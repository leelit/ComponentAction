package com.example.componentaction;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.componentaction.service.MyService;

public class CtrLocalServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] buttons = new int[]{R.id.start_normal_service, R.id.stop_normal_service, R.id.bind_normal_service, R.id.unbind_normal_service, R.id.binder_call, R.id.btn_jump_ctr_remote, R.id.service_callback, R.id.service_callback_newthread};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        for (Integer btn : buttons) {
            findViewById(btn).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(final View v) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(CtrLocalServiceActivity.this, MyService.class));
                intent.setAction("hello service");
                switch (v.getId()) {
                    case R.id.start_normal_service:
                        startService(intent);
                        break;

                    case R.id.stop_normal_service:
                        stopService(intent);
                        break;

                    case R.id.bind_normal_service:
                        bindService(intent, conn, BIND_AUTO_CREATE);
                        break;

                    case R.id.unbind_normal_service:
                        unbindService(conn);
                        break;

                    case R.id.binder_call:
                        if (myBinder == null){
                            Toast.makeText(CtrLocalServiceActivity.this, "please bind service first", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.i("test-service", "start call " + Utils.getCurInfo());
                        try {
                            myBinder.test();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Log.i("test-service", "end call " + Utils.getCurInfo());
                        break;

                    case R.id.service_callback:
                        try {
                            myBinder.callback();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.service_callback_newthread:
                        try {
                            myBinder.callbackInNewThread();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.btn_jump_ctr_remote:
                        // 废弃，在MyService处指定process来控制是否IPC
                        // startActivity(new Intent(CtrLocalServiceActivity.this, CtrRemoteServiceActivity.class));
                        break;
                }
            }
        };
        runnable.run();
//        new Thread(runnable, "---newBtnThread---").start();
    }


    private MyAidl myBinder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("test-service", "onServiceConnected " + Utils.getCurInfo());
            myBinder = MyAidl.Stub.asInterface(service);
            try {
                myBinder.registerCallback(new ServiceCallback.Stub() {
                    @Override
                    public void callback() throws RemoteException {
                        Log.i("test-service", "Client onServiceCallback " + Utils.getCurInfo());
                        SystemClock.sleep(2000);
                        Log.i("test-service", "Client onServiceCallback end " + Utils.getCurInfo());
                    }

                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("test-service", "onServiceDisconnected " + Utils.getCurInfo());
        }
    };
}

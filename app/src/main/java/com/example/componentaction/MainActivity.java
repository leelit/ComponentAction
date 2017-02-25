package com.example.componentaction;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] buttons = new int[]{R.id.btn_service_activity, R.id.btn_send_local_broadcast, R.id.btn_send_local_broadcast_newthread, R.id.btn_send_remote_broadcast};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (Integer btn : buttons) {
            findViewById(btn).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_service_activity:
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(MainActivity.this, CtrLocalServiceActivity.class));
                startActivity(intent);
                break;

            case R.id.btn_send_local_broadcast:
                Log.i("receiver", "start send local broadcast");
                sendBroadcast(new Intent("com.leelit.action.local"));
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(this, LocalReceiver.class));
//                sendBroadcast(intent);
                Log.i("receiver", "end send local broadcast");
                break;

            case R.id.btn_send_local_broadcast_newthread:
                Log.i("receiver", "start send local broadcast");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendBroadcast(new Intent("com.leelit.action.local"));
                    }
                }, "---newThread---").start();
                Log.i("receiver", "end send local broadcast");
                break;

            case R.id.btn_send_remote_broadcast:
                Log.i("receiver", "start send remote broadcast");
                sendBroadcast(new Intent("com.leelit.action.remote"));
                Log.i("receiver", "end send remote broadcast");
                break;
        }
    }
}

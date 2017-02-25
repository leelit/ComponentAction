package com.example.componentaction.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.componentaction.MyAidl;
import com.example.componentaction.ServiceCallback;
import com.example.componentaction.Utils;

/**
 * Created by kenjxli on 2017/2/25.
 */

public class MyService extends Service {

    private RemoteCallbackList<ServiceCallback> callbackList = new RemoteCallbackList<>();

    private IBinder aidl = new MyAidl.Stub() {

        @Override
        public void test() throws RemoteException {
            Log.i("test-service", "service#call sleep 2s, " + Utils.getCurInfo());
            SystemClock.sleep(2000);
        }

        @Override
        public void callback() throws RemoteException {
            Log.i("test-service", "start service Callback, " + Utils.getCurInfo());
            serviceCallback(false);
            Log.i("test-service", "end service Callback, " + Utils.getCurInfo());
        }

        @Override
        public void callbackInNewThread() throws RemoteException {
            Log.i("test-service", "start service Callback in ---newServiceThread---, " + Utils.getCurInfo());
            serviceCallback(true);
            Log.i("test-service", "end service Callback in ---newServiceThread---, " + Utils.getCurInfo());
        }

        @Override
        public void registerCallback(ServiceCallback callback) throws RemoteException {
            Log.i("test-service", "registerCallback, " + Utils.getCurInfo());
            callbackList.register(callback);
        }


    };

    private void serviceCallback(boolean newThread) {
        int size = callbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            final ServiceCallback callBack = callbackList.getBroadcastItem(i);
            if (callBack != null) {
                try {
                    if (!newThread) {
                        callBack.callback();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    callBack.callback();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, "---newServiceThread---").start();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        callbackList.finishBroadcast();
    }

    @Override
    public void onCreate() {
        Log.i("test-service", "onCreate " + Utils.getCurInfo());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("test-service", "onStartCommand " + Utils.getCurInfo());
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("test-service", "onBind " + Utils.getCurInfo());
        return aidl;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("test-service", "onUnbind " + Utils.getCurInfo());
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i("test-service", "onDestroy " + Utils.getCurInfo());
        super.onDestroy();
    }
}

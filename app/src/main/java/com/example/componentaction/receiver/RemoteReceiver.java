package com.example.componentaction.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.componentaction.Utils;

/**
 * Created by kenjxli on 2017/2/25.
 */

public class RemoteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("receiver", "remote receiver begin " + Utils.getCurInfo());
        SystemClock.sleep(2000);
        Log.i("receiver", "remote receiver end " + Utils.getCurInfo());
    }
}

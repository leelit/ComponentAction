package com.example.componentaction;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by kenjxli on 2017/2/25.
 */

public class Utils {

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static String getCurThread(){
        return Thread.currentThread().toString();
    }

    public static String getCurInfo(){
        return String.format("process:%s,thread:%s", getCurProcessName(MyApplication.context), getCurThread());
    }
}

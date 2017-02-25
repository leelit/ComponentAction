package com.example.componentaction;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by kenjxli on 2017/2/25.
 */

public class MyApplication extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Log.i("application", "start process: " + Utils.getCurProcessName(this));
    }
}

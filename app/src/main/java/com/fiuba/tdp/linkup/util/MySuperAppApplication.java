package com.fiuba.tdp.linkup.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by alejandro on 9/25/17.
 */

public class MySuperAppApplication extends Application {
    private static Application instance;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}

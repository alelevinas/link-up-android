package com.fiuba.tdp.linkup.components.AsyncTaskLoaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/**
 * Created by nicomoccagatta on 1/10/17.
 */

public class LoginAsyncTaskLoader extends android.support.v4.content.AsyncTaskLoader<String> {
    public LoginAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {
        try{
            Thread.sleep(250);
            Log.d("LOGIN LOADER-","Sleep finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "login successs";
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

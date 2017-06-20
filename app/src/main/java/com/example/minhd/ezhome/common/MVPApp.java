package com.t3h.basemvp.common;

import android.app.Application;
import android.content.Context;


/**
 * Created by ducnd on 5/18/17.
 */

public class MVPApp extends Application {
    private static Context mContextApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mContextApp = this;

    }

    public static Context getContextApp(){
        return mContextApp;
    }
}

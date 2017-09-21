package com.danny7899.dailytoast;

/**
 * Created by danny7899 on 1/7/16.
 */
import android.app.Application;

//import info.androidhive.parsenotifications.helper.ParseUtils;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // register with parse
        ParseUtils.registerParse(this);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
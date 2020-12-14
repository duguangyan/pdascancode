package com.hxb.pdascancode;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


/**
 * @author hxb
 */
public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static String topActivity = "com.hxb.pdascancode.MainActivity";

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        registerActivityLifecycleCallbacks(this);
    }


    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取最上层activity名称
     * @param tag
     * @return
     */
    public static boolean isTopActivity(String tag) {
        if (TextUtils.isEmpty(topActivity)) {
            return false;
        }
        return topActivity.contains(tag);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i("APP------------",activity.getClass().getName());
        topActivity = activity.getClass().getName();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}

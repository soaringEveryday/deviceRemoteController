package com.remote.controller;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;

import com.remote.controller.utils.CustomCrashHandler;
import com.remote.controller.utils.L;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Chen Haitao on 2015/7/25.
 */
public class CustomApplication extends Application{

    private HashMap<String, WeakReference<Activity>> activityList = new HashMap<String, WeakReference<Activity>>();

    public static Typeface typefaceRoboto;

    private static CustomApplication instance;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        CustomApplication application = (CustomApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    public static CustomApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        setAppFont();

        L.i(">>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<");
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidth = config.smallestScreenWidthDp;
        L.i("smallest width : " + smallestScreenWidth);

        //tencent bugly
//        CrashReport.initCrashReport(this, "900009289", true);

        LeakCanary.install(this);

        // 获取crash信息并写日志;
        CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
        mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void setAppFont() {
        typefaceRoboto = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        try {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, typefaceRoboto);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void addActivity(Activity activity) {
        if (null != activity) {
            L.d("********* add Activity " + activity.getClass().getName());
            activityList.put(activity.getClass().getName(), new WeakReference<>(activity));
        }
    }


    public void removeActivity(Activity activity) {
        if (null != activity) {
            L.d("********* remove Activity " + activity.getClass().getName());
            activityList.remove(activity.getClass().getName());
        }
    }

    public void exit() {
        printActivityStackInfo();
        for (String key : activityList.keySet()) {
            WeakReference<Activity> activity = activityList.get(key);
            if (activity != null && activity.get() != null) {
                L.d("********* Exit " + activity.get().getClass().getSimpleName());
                activity.get().finish();
            }
        }

        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void printActivityStackInfo() {
        L.d(" -- bottom --");
        for (String key : activityList.keySet()) {
            WeakReference<Activity> activity = activityList.get(key);
            if (activity != null) {
                Activity a = activity.get();
                if (a != null) {
                    L.d(" -- " + a.getClass().getSimpleName() + " --");
                }
            }
        }
        L.d(" -- top --");
    }
}

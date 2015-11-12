package com.qianmi.epos.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * Created by Chen Haitao on 2015/7/6.
 */
public class AppUtils {

    private AppUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTeleImei(Context context){
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager)context.getSystemService( Context.TELEPHONY_SERVICE );
        String imeistring = telephonyManager.getDeviceId();
        if(imeistring == null||imeistring.equals("")) {
            imeistring = getAndroidId(context);
        }
        if(imeistring == null||imeistring.equals("")) {
            imeistring = getMacId();
        }
        return imeistring;
    }
    public static String getAndroidId(Context context){
        return android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
    }

    public static String getMacId(){
        //String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class );
            serialnum = (String)(get.invoke(c, "ro.serialno", "unknown" ));
        }
        catch (Exception ignored)
        {
        }
        return serialnum;
    }

}

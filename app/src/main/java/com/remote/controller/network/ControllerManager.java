package com.remote.controller.network;

import android.content.Context;

import com.remote.controller.utils.L;

/**
 * Created by Chen Haitao on 2015/11/12.
 */
public class ControllerManager {

    private Context mContext;
    private static ControllerManager sInstance;


    public ControllerManager(Context context) {
        this.mContext = context;
    }

    public static ControllerManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ControllerManager(context);
        }
        return sInstance;
    }

    public void scanDevice() {
        L.d("ControllerManager -> scanDevice");

        ServerLocal searchLocal = new ServerLocal();
        searchLocal.ConnectDevice();
    }

}

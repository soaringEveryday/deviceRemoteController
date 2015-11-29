package com.remote.controller.network;

import android.content.Context;

import com.remote.controller.utils.L;
import com.remote.controller.utils.SPUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Chen Haitao on 2015/11/12.
 */
public class ControllerManager {

    private boolean isConnected = false;
    private Context mContext;
    private static ControllerManager sInstance;
    private String ip;
    private int port;
    private Socket mSocket;

    private ClientThread clientThread;


    public ControllerManager(Context context) {
        this.mContext = context;
    }

    public static ControllerManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ControllerManager(context);
        }
        return sInstance;
    }

    public void connectServer(String i, int p) {
        if (i.isEmpty()) {
            L.e("ip is empty");
            return;
        }

        this.ip = i;
        this.port = p;

        try {
            clientThread = new ClientThread(i, p);
            clientThread.start();
            SPUtils.put(mContext, "ip", i);
            SPUtils.put(mContext, "port", p);
            L.d("connected " + i + ":" + p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendData(byte[] data) {
        if (clientThread != null && clientThread.isRun && clientThread.isAlive()) {
            clientThread.send(data);
        } else {
            L.e("client thread is halt");
        }
    }

    public void stop() {
        if (clientThread != null && clientThread.isRun && clientThread.isAlive()) {
            clientThread.close();
        } else {
            L.e("client thread is halt when try to stop");
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getmSocket() {
        return mSocket;
    }

    public void setmSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }
}

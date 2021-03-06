package com.remote.controller.network;

import android.os.Looper;
import android.os.Message;

import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.L;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

import de.greenrobot.event.EventBus;

/**
 * Created by Chen Haitao on 2015/8/21.
 */
public class ClientThread extends Thread {

    private Socket s;
    private BufferedReader bff = null;
    private OutputStream ou = null;
    private InputStream in = null;
    public boolean isRun = true;
    private byte[] data = new byte[128];

    private String ip;
    private int port;

    public ClientThread(String i, int p)
            throws IOException {

        ip = i;
        port = p;
    }

    /**
     * 连接socket服务器
     */
    public int conn() {

        try {
            L.d("connecting : " + ip + ":" + port);
            s = new Socket(ip, port);
//            s.setSoTimeout(10000);// 设置阻塞时间
            s.setKeepAlive(true);
            // 防止粘包，即两条指令数据合并到一起后才发送出去
            // 发送数据包，默认为 false，即客户端发送数据采用 Nagle 算法；
            // 但是对于实时交互性高的程序，建议其改为 true，即关闭 Nagle 算法，客户端每发送一次数据，无论数据包大小都会将这些数据发送出去
            s.setTcpNoDelay(true);
            //写数据不放人缓冲
//            s.sendUrgentData(0x44);//"D" s
            L.d("connect successfully, then create client thread");
            init();
        } catch (UnknownHostException e) {
            L.d("连接错误UnknownHostException 重新获取");
            e.printStackTrace();
            conn();
        } catch (IOException e) {
            L.d("连接服务器io错误");
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            L.d("连接服务器错误Exception" + e.getMessage());
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    /**
     * 连接socket服务器
     */
    public void init() {

        try {
            L.d("获取输入输出流");
            ou = s.getOutputStream();
            in = s.getInputStream();
            bff = new BufferedReader(new InputStreamReader(in));
            L.d("输入输出流获取成功");
            notifyConnected();
        } catch (UnknownHostException e) {
            L.d("连接错误UnknownHostException 重新获取");
            e.printStackTrace();
            init();
        } catch (IOException e) {
            L.d("连接服务器io错误");
            e.printStackTrace();
        } catch (Exception e) {
            L.d("连接服务器错误Exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Looper.prepare();
        try {
            if (conn() < 0) {
                L.e("init or connect socket error");
                isRun = false;
            }
            int bytesRead = -1;
            while (isRun) {
                if ((bytesRead = in.read(data)) != -1) {
                    L.d("recieve " + bytesRead + "bytes data\n");
                    String text = new String(data).trim();
//                    dispatchMessage(text);
                    dispatchMessage(data, bytesRead);
                } // while
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            notifyTimeout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Looper.loop();
    }

    private void notifyConnected() {
        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_SOCKET_CONNECTED;
        EventBus.getDefault().postSticky(msg);
    }

    private void notifyDisConnected() {
        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_SOCKET_DISCONNECTED;
        EventBus.getDefault().postSticky(msg);
    }

    private void notifyTimeout() {
        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_SOCKET_TIMEOUT;
        EventBus.getDefault().postSticky(msg);
    }

    private void dispatchMessage(byte[] data, int length) {
        L.v("dispatch text from server socket : " + Arrays.toString(data));
        EventParser.getInstance().parse(data, length);
    }

    private void dispatchMessage(String text) {
        L.v("dispatch text from server socket : " + text);
//        Message msg = Message.obtain();
//        msg.what = MessageEvent.MSG_SOCKET_RECEIVE_DATA;
//        msg.obj = text;
//        EventBus.getDefault().postSticky(msg);
        EventParser.getInstance().parse(text.getBytes(), text.getBytes().length);
    }

    /**
     * 发送数据
     *
     */
    public synchronized void send(byte[] buffer) {
        try {
            if (ou != null) {
                ou.write(buffer);
                ou.flush();
                L.d("sent to server : " + Arrays.toString(buffer));
            } else {
                L.e("ou is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        isRun = false;
        try {
            L.d("stopping connection");
            if (in != null) {
                in.close();
            }
            if (ou != null) {
                ou.close();
            }
            if (bff != null) {
                bff.close();
            }
            if (s != null && !s.isClosed()) {
                s.close();
                s = null;
            }

            notifyDisConnected();
        } catch (Exception e) {
            L.d( "close err");
            e.printStackTrace();
        }

    }
}

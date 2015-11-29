package com.remote.controller.network;

import android.os.Message;

import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.L;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import de.greenrobot.event.EventBus;

/**
 * UDP广播线程
 * Created by Chen Haitao on 2015/11/29.
 */
public class BroadCastUdpThread extends Thread {

    private static int DEFAULT_PORT = 45454;
    private String dataString;
    DatagramSocket udpSocket;
    byte[] buffer = null;

    public BroadCastUdpThread(String dataString) {
        this.dataString = dataString;
    }

    @Override
    public void run() {

        L.v("BroadCastUdpThread is running");

        DatagramPacket dataPacket = null;

        try {
            buffer = dataString.getBytes();
            L.d("dataString is : " + dataString);
            udpSocket = new DatagramSocket(45455);
            udpSocket.setBroadcast(true);
            dataPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), 45454);
            if (dataPacket != null) {
                L.d("dataPacket is ok");
            } else {
                L.e("dataPacket create failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            L.e("1" + e.toString());
        }

        try {
            if (dataPacket == null) {
                L.e("data packet is null");
                return;
            }
            udpSocket.send(dataPacket);

            L.i("udp socket sent");

            DatagramPacket dpIn = null;
            byte[] bufferIn = new byte[256];
            dpIn = new DatagramPacket(bufferIn, bufferIn.length);
            L.i("before receive");
            udpSocket.receive(dpIn);
            L.i("after receive");
            String address = dpIn.getAddress().getHostAddress();
            L.i("address : " + address);
            String resStr = new String(bufferIn, 0, buffer.length).trim();
            L.d("recv : " + resStr);

            if (resStr.equals(Constant.ScanText.RES)) {
                Message msg = Message.obtain();
                msg.what = MessageEvent.MSG_SCAN_PAIR;
                msg.obj = address;
                EventBus.getDefault().postSticky(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            L.e("2" + e.toString());
        }
        if (udpSocket != null)
            udpSocket.close();
    }

}

package com.remote.controller.network;

import com.remote.controller.utils.L;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chen Haitao on 2015/11/26.
 */
public class ServerLocal {

    private static final String UDP_SCAN_STR = "Searching C4_Robot...";

    private InetAddress m_BroadcastAddr = null;
    private Thread thread;
    private boolean m_bBroadcastStarted = false;
    private byte[] m_BroadcastBuffer = new byte[1024];

    private static String LocalAddress = "234.55.55.55";
    private static int LocalPort = 45454;
    private MulticastSocket m_MulticastSocket;

    private Timer m_PlayTimer;
    private TimerTask m_PlayTimerTask;

    /* 开启连接，加入局域网 */
    public void ConnectDevice() {
        try {
            m_BroadcastAddr = InetAddress.getByName(LocalAddress);
            m_MulticastSocket = new MulticastSocket(LocalPort);
            m_MulticastSocket.joinGroup(m_BroadcastAddr);
            m_MulticastSocket.setTimeToLive(255);
            m_MulticastSocket.setBroadcast(true);
            startReceiveThread();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 打开接收和搜索线程 */
    public void startReceiveThread() {

        m_PlayTimer = new Timer();

        m_PlayTimerTask = new TimerTask() {
            public void run() {
                SearchDevice();
            }

        };
        m_PlayTimer.schedule(m_PlayTimerTask, 0, 10000);// 十秒搜索一次

        if (thread == null) {
            thread = new Thread(null, doBackgroundThreadProcessing, "SocketThread");
            thread.start();
        }
    }

    public Runnable doBackgroundThreadProcessing = new Runnable() {
        public void run() {
            backgroundThreadProcessing();
        }
    };

    public void backgroundThreadProcessing() {
        m_bBroadcastStarted = true;
        while (m_bBroadcastStarted) {
            DatagramPacket packet = new DatagramPacket(m_BroadcastBuffer, m_BroadcastBuffer.length);
            try {
                if (m_MulticastSocket == null) {
                    continue;
                }
                L.d("before receive...");
                m_MulticastSocket.receive(packet);
                L.d("after receive...");
                L.d("host address : " + packet.getAddress().getHostAddress());
                byte[] data = packet.getData();
                L.d("data : " + new String(data).trim());

            } catch (IOException e) {
                // e.printStackTrace();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSearchDevice() {
        m_bBroadcastStarted = false;

        if (m_PlayTimer != null) {
            m_PlayTimer.cancel();
        }
        if (m_PlayTimerTask != null) {
            m_PlayTimerTask.cancel();
        }

        if (m_MulticastSocket != null) {
            m_MulticastSocket.close();
            m_MulticastSocket = null;
        }

    }

    public void SearchDevice() {

        byte[] cmdBuff = UDP_SCAN_STR.getBytes();

        try {
            DatagramPacket dataPacket = new DatagramPacket(cmdBuff, cmdBuff.length, m_BroadcastAddr, LocalPort);
            m_MulticastSocket.send(dataPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

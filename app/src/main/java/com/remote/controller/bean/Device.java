package com.remote.controller.bean;

/**
 * 远端服务器
 *
 * Created by Chen Haitao on 2015/11/13.
 */
public class Device {

    /**
     * ip地址
     */
    private String ip;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 描述
     */
    private String desc;

    public Device(String ip, String deviceName, String desc) {
        this.ip = ip;
        this.deviceName = deviceName;
        this.desc = desc;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

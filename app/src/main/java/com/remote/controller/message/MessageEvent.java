package com.remote.controller.message;

/**
 * Define your MSG is first on the MessageEvent
 * <p/>
 * This MessageEvent is used by event bus post action
 * When need send out a signal to notify event bus registered function,
 * just call EventBus.getDefault().pos(new MessageEvent({MSG id}))
 * <p/>
 * Created by Chen Haitao on 2015/8/10.
 */
public class MessageEvent {

    public static final int MSG_CREATE_NEW_FILE_SUCCESS = 100;    //创建新文件信息获得文件描述、文件名等信息
    public static final int MSG_COMMAND_UPDATE = 101;               //指令数据集更新
    public static final int MSG_COMMAND_CLEAR = 102;               //指令数据集清空通知，用于新建文件时
    public static final int MSG_SOCKET_RECEIVE_DATA = 103;          //从server socket接收数据
    public static final int MSG_SOCKET_TIMEOUT =  104;              //超时
    public static final int MSG_SOCKET_CONNECTED =  105;              //连接成功
    public static final int MSG_SOCKET_DISCONNECTED =  106;              //断开成功
    public static final int MSG_SCAN_PAIR = 107;                    //扫描匹配成功

}

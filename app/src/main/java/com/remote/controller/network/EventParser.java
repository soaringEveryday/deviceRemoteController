package com.remote.controller.network;

import android.os.Message;

import com.remote.controller.constant.Constant;
import com.remote.controller.message.MessageEvent;
import com.remote.controller.utils.L;

import java.util.Arrays;

import de.greenrobot.event.EventBus;

/**
 * Created by Chen Haitao on 2015/11/27.
 */
public class EventParser {

    private final int MAX_DATA_LEN = 128;

    private static EventParser sInstance;

    private byte[] data;

    public EventParser() {
        data = new byte[MAX_DATA_LEN];
    }

    public static EventParser getInstance() {
        if (sInstance == null) {
            sInstance = new EventParser();
        }
        return sInstance;
    }

    public void parse(byte[] data) {
        L.d("parse, check enter : \n" + Arrays.toString(data));
        int length = data.length;
        if (length <= 0) {
            L.e("parsed data is empty");
            return;
        }

        //检查类型
        int type = byte2int(data[0]);
        if (type != Constant.Type.EVENT_REMOTE_RES) {
            L.e("服务器返回类型错误:" + type);
            return;
        }

        //获取长度
        byte[] lengthByte = new byte[4];
        lengthByte[0] = data[1];
        lengthByte[1] = data[2];
        lengthByte[2] = 0x0;
        lengthByte[3] = 0x0;
        int lengthLeft = byte2int(lengthByte);
        L.d("lengthLeft : " + lengthLeft);

        //获得返回功能码
        int funcCode = byte2int(data[3]);
        L.d("funcCode : " + funcCode);

        //获得执行结果
        int result = byte2int(data[4]);
        if (result != 0) {
            L.e("error response, code : " + result);
            return;
        }

        //获得返回数据
        byte[] resultData = Arrays.copyOfRange(data, 5, 5 + lengthLeft - 4);
        L.d("result data :\n" + Arrays.toString(resultData));

        dispatchResponse(funcCode, resultData);

    }

    private void dispatchResponse(int funcCode, byte[] data) {
        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_SOCKET_RECEIVE_DATA;
        msg.arg1 = funcCode;
        msg.obj = data;
        EventBus.getDefault().postSticky(msg);
    }

    private int byte2int(byte[] res) {
        // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

        int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
                | ((res[2] << 24) >>> 8) | (res[3] << 24);
        return targets;
    }

    private int byte2int(byte b) {
        return  b & 0xff;
    }


}

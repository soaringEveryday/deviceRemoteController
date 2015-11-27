package com.remote.controller.network;

import com.remote.controller.constant.Constant;
import com.remote.controller.utils.L;

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

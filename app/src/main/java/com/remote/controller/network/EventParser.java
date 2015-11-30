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

    public EventParser() {
    }

    public static EventParser getInstance() {
        if (sInstance == null) {
            sInstance = new EventParser();
        }
        return sInstance;
    }

    public synchronized void parse(byte[] data, int length) {
        L.d("parse, check enter : \n" + Arrays.toString(data));
        if (length <= 0) {
            L.e("parsed data is empty");
            return;
        }

        int cursor = 0;

        while (cursor < length - 1) {
            //检查类型
            int type = byte2int(data[cursor++]);//cursor after = 1
            //目前仅解析该两种返回类型
            if (type != Constant.Type.EVENT_REMOTE_RES && type != Constant.Type.FILE_REMOTE_RES) {
                L.e("服务器返回类型错误:" + type);
                return;
            }

            if (type == Constant.Type.EVENT_REMOTE_RES) {
                //事件数据包返回
                //获取长度
                byte[] lengthByte = new byte[4];
                lengthByte[0] = data[cursor++];//cursor after = 2
                lengthByte[1] = data[cursor++];//cursor after = 3
                lengthByte[2] = 0x0;
                lengthByte[3] = 0x0;
                int lengthLeft = byte2int(lengthByte);
                L.d("lengthLeft : " + lengthLeft);

                //获得返回功能码
                int funcCode = byte2int(data[cursor++]);//cursor after = 4
                L.d("funcCode : " + funcCode);

                //获得执行结果
                int result = byte2int(data[cursor++]);////cursor after = 5
                if (result != 0) {
                    L.e("error response, code : " + result);
                    return;
                }

                //获得返回数据
                if (lengthLeft == 4) {
                    L.d("a");
                    //说明返回数据只有一个字节
                    byte[] resultData = new byte[2];
                    resultData[0] = data[cursor++];//cursor after = 6
                    resultData[1] = 0x0;
                    dispatchResponse(funcCode, resultData, 1);
                    L.d("cursor : " + cursor);
                    cursor++; // 跳过一个检验和
                    L.d("cursor : " + cursor);
                } else if (lengthLeft > 4) {
                    L.d("b");
                    //说明返回数据大于一个字节
                    byte[] resultData = Arrays.copyOfRange(data, cursor, cursor + lengthLeft - 3);
                    L.d("result data :\n" + Arrays.toString(resultData));

                    dispatchResponse(funcCode, resultData, resultData.length);

                    L.d("cursor : " + cursor);

                    cursor += resultData.length + 1; // 跳过返回数据和一个字节检验和长度

                    L.d("cursor : " + cursor);

                } else {
                    L.d("c");
                    //返回数据为0字节
                    cursor++; // 跳过一个检验和
                }

            } else {
                //文件传送返回
            }
        }

    }

    private void dispatchResponse(int funcCode, byte[] data, int length) {
        Message msg = Message.obtain();
        msg.what = MessageEvent.MSG_SOCKET_RECEIVE_DATA;
        msg.arg1 = funcCode;
        msg.arg2 = length;
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
        return b & 0xff;
    }


}

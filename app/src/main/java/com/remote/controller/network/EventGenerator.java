package com.remote.controller.network;

import com.remote.controller.constant.Constant;
import com.remote.controller.utils.L;

import java.util.Arrays;

/**
 * Created by Chen Haitao on 2015/11/27.
 */
public class EventGenerator {

    private final int MAX_DATA_LEN = 256;
    private final int MAX_DATA_FILE_LEN = 2048;

    private static EventGenerator sInstance;

    private byte[] data;
    private byte[] fileBytes;

    public EventGenerator() {
        data = new byte[MAX_DATA_LEN];
        fileBytes = new byte[MAX_DATA_FILE_LEN];
    }

    public static EventGenerator getInstance() {
        if (sInstance == null) {
            sInstance = new EventGenerator();
        }
        return sInstance;
    }

    public byte[] generateData(int funcCode, String parameter) {
        reset();
        data[0] = (byte) (Constant.Type.EVENT_REQ & 0xff);//类型 3

        byte[] parameterBytes = new byte[1024];
        int parameterLength = 0;
        if (parameter != null) {
            parameterBytes = parameter.getBytes();
            parameterLength = parameterBytes.length;
        }

        int dataLength = 1 + parameterLength + 1; //1位功能码长度 + 参数长度 + 检验和长度

        data[1] = (byte) (dataLength & 0xff);
        data[2] = (byte) ((dataLength >> 8) & 0xff);//长度

        //功能码
        data[3] = (byte) (funcCode & 0xff);

        //参数
        if (parameterLength > 0) {
            //TODO 参数字节掉转， 低位在前
//            for (int i = 4 ; i < parameterLength ; i++) {
//                data[i] = parameterBytes[i - 4];
//            }
            for (int i = 0; i < parameterLength; i++) {
                data[i + 4] = parameterBytes[i];
            }
        }

        //检验和
        byte sum = 0;
        for (int i = 0 ; i < 4 + parameterLength ; i++) {
            sum += data[i];
        }
        data[4 + parameterLength] = sum;

        return Arrays.copyOf(data, 5 + parameterLength);
    }

    public byte[] generateData(int funcCode, int parameter) {
        reset();
        data[0] = (byte) (Constant.Type.EVENT_REQ & 0xff);//类型 3

        byte[] parameterBytes;
        int parameterLength = 0;
        parameterBytes = int2byte(parameter);
        parameterLength = parameterBytes.length;
        L.d("--> parameterLength : " + parameterLength);

        int dataLength = 1 + parameterLength + 1; //1位功能码长度 + 参数长度 + 检验和长度

        data[1] = (byte) (dataLength & 0xff);
        data[2] = (byte) ((dataLength >> 8) & 0xff);//长度

        //功能码
        data[3] = (byte) (funcCode & 0xff);

        //参数
        if (parameterLength > 0) {
            //TODO 参数字节掉转， 低位在前
            for (int i = 0; i < parameterLength; i++) {
                data[i + 4] = parameterBytes[i];
            }
        }

        //检验和
        byte sum = 0;
        for (int i = 0 ; i < 4 + parameterLength ; i++) {
            sum += data[i];
        }
        data[4 + parameterLength] = sum;

        return Arrays.copyOf(data, 5 + parameterLength);
    }

    public byte[] generateDataByBytes(int funcCode, byte[] parameterBytes) {
        reset();
        data[0] = (byte) (Constant.Type.EVENT_REQ & 0xff);//类型 3

        int parameterLength = 0;
        parameterLength = parameterBytes.length;
        L.d("--> parameterLength : " + parameterLength);

        int dataLength = 1 + parameterLength + 1; //1位功能码长度 + 参数长度 + 检验和长度

        data[1] = (byte) (dataLength & 0xff);
        data[2] = (byte) ((dataLength >> 8) & 0xff);//长度

        //功能码
        data[3] = (byte) (funcCode & 0xff);

        //参数
        if (parameterLength > 0) {
            //TODO 参数字节掉转， 低位在前
            for (int i = 0; i < parameterLength; i++) {
                data[i + 4] = parameterBytes[i];
            }
        }

        //检验和
        byte sum = 0;
        for (int i = 0 ; i < 4 + parameterLength ; i++) {
            sum += data[i];
        }
        data[4 + parameterLength] = sum;

        return Arrays.copyOf(data, 5 + parameterLength);
    }

    public byte[] generateFile(String fileData) {
        resetFile();
        fileBytes[0] = (byte) (Constant.Type.FILE_REQ & 0xff);//类型 2

        byte[] parameterBytes = new byte[2048];
        int parameterLength = 0;
        if (fileData != null) {
            parameterBytes = fileData.getBytes();
            parameterLength = parameterBytes.length;
        }

        int length = parameterLength + 1; //参数长度 + 检验和长度

        fileBytes[1] = (byte) (length & 0xff);
        fileBytes[2] = (byte) ((length >> 8) & 0xff);//长度

        //参数
        if (parameterLength > 0) {
            //TODO 参数字节掉转， 低位在前
            for (int i = 0; i < parameterLength; i++) {
                fileBytes[i + 3] = parameterBytes[i];
            }
        }

        //检验和
        byte sum = 0;
        for (int i = 0 ; i <3 + parameterLength ; i++) {
            sum += fileBytes[i];
        }
        fileBytes[3 + parameterLength] = sum;

        return Arrays.copyOf(fileBytes, 4 + parameterLength);
    }

    public byte[] generateFile(byte[] parameterBytes) {
        resetFile();
        fileBytes[0] = (byte) (Constant.Type.FILE_REQ & 0xff);//类型 2

        int parameterLength = 0;
        if (parameterBytes != null) {
            parameterLength = parameterBytes.length;
        }

        int length = parameterLength + 1; //参数长度 + 检验和长度

        fileBytes[1] = (byte) (length & 0xff);
        fileBytes[2] = (byte) ((length >> 8) & 0xff);//长度

        //参数
        if (parameterLength > 0) {
            //TODO 参数字节掉转， 低位在前
            for (int i = 0; i < parameterLength; i++) {
                fileBytes[i + 3] = parameterBytes[i];
            }
        }

        //检验和
        byte sum = 0;
        for (int i = 0 ; i <3 + parameterLength ; i++) {
            sum += fileBytes[i];
        }
        fileBytes[3 + parameterLength] = sum;

        return Arrays.copyOf(fileBytes, 4 + parameterLength);
    }

    private byte[] int2byte(int res) {
        byte[] targets = new byte[4];

        targets[0] = (byte) (res & 0xff);// 最低位
        L.d("-- > " + targets[0]);
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        L.d("-- > " + targets[1]);
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        L.d("-- > " + targets[2]);
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        L.d("-- > " + targets[3]);

        return targets;
    }

    private void reset() {
        int lengh = data.length;
        for (int i = 0 ; i<lengh ; i++) {
            data[0] = 0x0;
        }
    }

    private void resetFile() {
        int lengh = fileBytes.length;
        for (int i = 0 ; i<lengh ; i++) {
            fileBytes[0] = 0x0;
        }
    }

    private byte int2OneByte(int res) {
        return (byte) (res & 0xff);
    }
}

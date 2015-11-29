package com.remote.controller.constant;

/**
 * Created by Chen Haitao on 2015/7/7.
 */
public class Constant {

    public static String TOKEN_TYPE = "token_type";
    public static String ACCESSTOKEN = "accessToken";
    public static String ACCESS_TOKEN = "access_token";
    public static String METHOD = "method";
    public static String APPKEY = "appKey";
    public static String FORMAT = "format";
    public static String V = "v";
    public static String SIGN = "sign";
    public static String TIMESTAMP = "timestamp";
    public static String UPLOADFILE = "uploadFile";
    public static String _SIGN = "_sign";
    public static String MESSAGE_OK = "ok";
    public static String NICK_NAME = "nick_name";
    public static String EMP_NICK_NAME = "emp_nick_name";
    public static String PASSWORD = "password";
    public static String FIELDS = "fields";
    public static String APPLY_ID = "apply_id";
    public static String DTYPE = "dtype";
    public static String LOGIN_USER = "login.user";
    public static String LOGIN_PWD = "login.pwd";
    public static String OPERATOR_NAME = "operator.nameUp";
    public static String DEVICE_ID = "device.id";
    public static String STORE_NAME = "store.nameUp";
    public static String STORE_PHONE = "store.phone";
    public static String P_PWD = "p.pwd";
    public static String P_NICK_NAME = "p.nick.name";

    public static String EMPLOYYE_START_LETTER = "E";
    public static String ADMIN_ID = "ADMIN_ID";
    public static String TOKEN = "TOKEN";
    public static String USER_ID = "USER_ID";
    public static String USER_NAME = "USER_NAME";
    public static String UMENG_TOKEN = "UMENG_TOKEN";
    public static String P_USER_ID = "P_USER_ID";
    public static String USER_TYPE = "USER_TYPE";
    public static String LEVEL_ID = "LEVEL_ID";

    public static class ErrorCode {
        public static final String NINE = "9";
        public static final String ONEHUNDREDANDONE = "101";
        public static final String ONEHUNDREDANTWO = "102";
    }

    public static class ResultCode {
        public static final int MEMBER_NUMBER = 98;
        public static final int POP_ORDER_CODE = 99;
        public static final int GOODS_REFRESH_CODE = 100;
        public static final int PRODUCT_DELETE_CODE = 101;
        public static final int START_DOWNLOAD_REQUEST = 400;
        public static final int OVER_DOWNLOAD = 401;
        public static final int ERROR_DOWNLOAD = 402;
        public static final int REFRESH_DOWNLOAD = 403;
        public static final int REFRESH_ALL_GOODS = 4004;
    }

    public static class RequestCode {
        public static final int ADD_SELF_GOODS = 300;
        public static final int REFRESH_GOOD = 301;
    }

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 运动指令参数 x, y, z, a, v , g
     */
    public static class Param {
        public static final String X = "x";
        public static final String Y = "y";
        public static final String Z = "z";
        public static final String A = "a";
        public static final String V = "v";
        public static final String G = "g";
    }

    /**
     * 指令类型
     */
    public static class Command {
        public static final String LINE_TO = "LineTo";
        public static final String MOVE_TO = "MoveTo";
        public static final String SET_DO = "SetDO";
        public static final String WAIT_DI = "WaitDI";
        public static final String DELAY = "Delay";
        public static final String LINE_TO_VEL = "LineToVel";
        public static final String MOVE_TO_VEL = "MoveToVel";
    }

    /**
     * 文件类型版本
     */
    public static class FileFormat {
        public static final int VERION_CSV = 1;
    }

    /**
     * 存储文件用share preference key
     */
    public static class SPKEY {
        public static final String FILE_NAME = "key_file_name";
        public static final String FILE_DESC = "key_file_desc";

    }

    public static class EventCode {
        public static final int READ_DEVICE_NAME = 1;
        public static final int READ_DEVICE_DESC = 2;
        public static final int READ_RUNNING_STATE = 3;
        public static final int READ_DATA_ON_SETTING = 4;
        public static final int READ_DATA_ON_PLAY = 5;
        public static final int READ_PLAY_TIMES = 6;
        public static final int READ_INPUT = 7;
        public static final int READ_OUTPUT = 8;
        public static final int BTN_DOWN_SETTING_BUTTON= 9;
        public static final int BTN_UP_SETTING_BUTTON = 10;
        public static final int BTN_DOWN_IO_OUTPUT = 11;
        public static final int BTN_PLAY_LAUNCH = 12;
        public static final int BTN_PLAY_PAUSE = 13;
        public static final int BTN_PLAY_STOP = 14;
        public static final int BTN_PLAY_RESET = 15;
    }

    /**
     * 类型码
     */
    public static class Type {
        public static final int FILE_REQ = 2; // 文件发送请求
        public static final int EVENT_REQ = 3;//事件发送请求
        public static final int EVENT_REMOTE_RES = 6;//事件数据PC端回复
        public static final int FILE_REMOTE_RES = 7;//文件PC端回复

    }

    /**
     * 设备状态
     * 0 未连接
     * 1 空闲
     * 2 运行中
     * 3 出错
     */
    public static class RunningStatus {
        public static final int NO_CONNECTION = 0;
        public static final int IDLE = 1;
        public static final int RUNNING = 2;
        public static final int ERROR = 3;
    }

    /**
     * 同步时间频率 （ms）
     */
    public static int SYNC_SEQ_TIME = 5000;

    public static class ScanText {
        public static final String REQ = "Searching C4_Robot...";
        public static final String RES = "This is C4_Robot.";
    }
}

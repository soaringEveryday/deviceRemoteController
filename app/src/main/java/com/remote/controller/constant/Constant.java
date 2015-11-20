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

    public static class RunningStatus {
        public static final int NO_CONNECTION = 0;
        public static final int IDLE = 1;
        public static final int RUNNING = 2;
        public static final int ERROR = 3;
    }

    //运动指令参数 x, y, z, a, v , g
    public static class Param {
        public static final String X = "x";
        public static final String Y = "y";
        public static final String Z = "z";
        public static final String A = "a";
        public static final String V = "v";
        public static final String G = "g";
    }

    public static class FileFormat {
        public static final int VERION_CSV = 1;
    }

    public static class SPKEY {
        public static final String FILE_NAME = "key_file_name";
        public static final String FILE_DESC = "key_file_desc";

    }
}

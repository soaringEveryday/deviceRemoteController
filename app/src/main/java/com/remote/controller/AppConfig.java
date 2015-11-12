package com.qianmi.epos;


import com.qianmi.epos.constant.Constant;
import com.qianmi.epos.utils.SPUtils;
import com.qianmi.epos.utils.StrUtils;

/**
 * App配置类
 */
public final class AppConfig {

    // 线上环境;
    public static String ADMIN_ID = "A917384"; //maomaotest
    public static final String SERVER_URL = "http://posapi.qianmi.com/";
//    public static String ADMIN_ID = "A967550";  //天天买 andy2014
    public static final String IMAGE_SERVER_URL = "http://www.qmimg.com/";


    public static final String SIGN_SECRET = "secret";

    public static final String RELEASE_MPOS_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSav88Gx7su6zP+T+LxLwVtAdOb5Vdh/W9zNHDabKNyUnED76qE2HU2G3ww8lxqC9OFuhI0Oe2tuw1p/Z7j2JajLarDIJTL8eGGDhzIQNBH8GXWnLgG0Gw+H78+3J+F0SXF6qWXSL+0oV2lEI1nChq6tGoTP7z3w0oWYcwIPtF2QIDAQAB";

    // 请不要直接使用，使用下面方法掉用;
    private static String TOKEN = "";
    private static String USER_ID = "";
    private static String USER_NAME = "";
    private static String UMENG_TOKEN = "";
    private static String P_USER_ID = "";
    private static String USER_TYPE = "";
    private static String LEVEL_ID = "";
    private static String STORE_NAME = "";
    private static String P_PWD = "";
    private static String P_NICK_NAME = "";

    // 使用下面方法获取参数，以防止crash重启应用static参数被清空;
    public static void setAdminId(String adminId) {
        ADMIN_ID = adminId;
        SPUtils.put(CustomApplication.getInstance(), Constant.ADMIN_ID, adminId);
    }

    public static String getAdminId() {
        return (String) SPUtils.get(CustomApplication.getInstance(), Constant.ADMIN_ID, ADMIN_ID);
    }

    public static void setUserId(String userId) {
        USER_ID = userId;
        SPUtils.put(CustomApplication.getInstance(), Constant.USER_ID, userId);
    }

    public static String getUserId() {
        if (StrUtils.isNull(USER_ID)) {
            USER_ID = (String) SPUtils.get(CustomApplication.getInstance(), Constant.USER_ID, USER_ID);
        }
        return USER_ID;
    }

    public static void setToken(String key) {
        AppConfig.TOKEN = key;
        SPUtils.put(CustomApplication.getInstance(), Constant.TOKEN, key);
    }

    public static String getToken() {
        if (StrUtils.isNull(TOKEN)) {
            TOKEN = (String) SPUtils.get(CustomApplication.getInstance(), Constant.TOKEN, TOKEN);
        }
        return TOKEN;
    }

}

package com.jiujiu.autosos.common;

import com.code19.library.SystemUtils;

/**
 * 常数类
 */
public class Constant {

    public static final boolean DEBUG = true;

    /**
     * 是否强制走小米推送
     */
    public static final boolean FORCE_MIPUSH = true;

    public static final int PAGESIZE = 20;

    public static final String CODE_SUCCESS = "600000";

    public static final String CODE_SESSION_TIME_OUT = "6001001";//被踢

    public static final String KEY_APPID = "appId";

    public static final String KEY_PLATFORM = "platform";

    public static final String KEY_VERSION = "version";

    public static final String KEY_VERSIONCODE = "versionCode";

    public static final String APPID = "103";

    public static final String PLATFORM = "2";

    public static String VERSION;

    public static String VERSIONCODE;

    static {
        VERSION = SystemUtils.getAppVersionName(AutososApplication.getApp());
        VERSIONCODE = String.valueOf(SystemUtils.getAppVersionCode(AutososApplication.getApp()));
    }
}

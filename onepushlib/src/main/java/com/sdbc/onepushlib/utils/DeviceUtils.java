package com.sdbc.onepushlib.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * 设备工具类
 * author:yangweiquan
 * create:2017/5/4
 */
public class DeviceUtils {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_IS_MIUI = "is_miui";

    /**
     * 是否为小米系统
     * @return
     */
    public static boolean isMIUI(Context context) {
        SPUtils cacheSP = SPCacheUtils.getInstance(context).getCacheSP();
        if (cacheSP.contains(KEY_IS_MIUI)) {
            return cacheSP.getBoolean(KEY_IS_MIUI, false);
        }
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            boolean isMIUI = properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
            cacheSP.put(KEY_IS_MIUI, isMIUI);
            return isMIUI;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

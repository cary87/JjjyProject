package com.jiujiu.autosos.common.utils;

import android.util.Log;

import com.jiujiu.autosos.common.Constant;

/**
 * 日志打印工具类
 */
public class LogUtils {

    public static void i(String TAG, String content) {
        if (Constant.DEBUG) {
            Log.i(TAG, content);
        }
    }

    public static void e(String TAG, String content) {
        if (Constant.DEBUG) {
            Log.e(TAG, content);
        }
    }

}

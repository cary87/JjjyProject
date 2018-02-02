package com.sdbc.onepushlib.utils;

import android.util.Log;

import com.sdbc.onepushlib.bean.Config;

/**
 * author:yangweiquan
 * create:2017/5/5
 */
public class PushLogs {

    public static void d(String tag, String log) {
        if (Config.getInstance().isDebug()) {
            Log.d(tag, log);
        }
    }

    public static void e(String tag, String log) {
        if (Config.getInstance().isDebug()) {
            Log.e(tag, log);
        }
    }

    public static void e(String tag, String log, Throwable tr) {
        if (Config.getInstance().isDebug()) {
            Log.e(tag, log, tr);
        }
    }

    public static void w(String tag, String log) {
        if (Config.getInstance().isDebug()) {
            Log.w(tag, log);
        }
    }

    public static void w(String tag, String log, Throwable tr) {
        if (Config.getInstance().isDebug()) {
            Log.w(tag, log, tr);
        }
    }
}

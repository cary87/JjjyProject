package com.sdbc.onepushlib.utils;

import android.content.Context;

/**
 * author:yangweiquan
 * create:2017/5/4
 */
public class SPCacheUtils {

    private static final String FILENAME_CACHE = "cache_data";
    private static SPCacheUtils mInstance = null;
    private final SPUtils mSPUtils;
    private Context mContext;

    private SPCacheUtils(Context context) {
        this.mContext = context.getApplicationContext();
        mSPUtils = new SPUtils(mContext, FILENAME_CACHE);
    }

    public static SPCacheUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SPCacheUtils.class) {
                if (mInstance == null) {
                    mInstance = new SPCacheUtils(context);
                }
            }
        }

        return mInstance;
    }

    public SPUtils getCacheSP() {
        return mSPUtils;
    }
}

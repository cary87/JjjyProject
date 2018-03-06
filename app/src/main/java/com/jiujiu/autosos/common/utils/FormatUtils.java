package com.jiujiu.autosos.common.utils;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/3/6.
 */

public class FormatUtils {
    private FormatUtils() {

    }

    public static String formatMobilePhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }
        return phone;
    }

}

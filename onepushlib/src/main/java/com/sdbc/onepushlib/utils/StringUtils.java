package com.sdbc.onepushlib.utils;

/**
 * author:yangweiquan
 * create:2017/4/28
 */
public class StringUtils {

    public static boolean isNotBlank(CharSequence string) {
        if(string == null) {
            return false;
        } else {
            int i = 0;

            for(int n = string.length(); i < n; ++i) {
                if(!Character.isWhitespace(string.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isEmpty(CharSequence string) {
        return !isNotBlank(string);
    }
}

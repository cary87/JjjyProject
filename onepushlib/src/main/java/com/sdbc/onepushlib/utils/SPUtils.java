package com.sdbc.onepushlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * SP相关工具类
 * author:yangweiquan
 * create:2017/5/3
 */
public final class SPUtils {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    /**
     * SPUtils构造函数
     */
    public SPUtils(Context context, String spName) {
        sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.apply();
    }

    public void put(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    /**
     * SP中读取Boolean
     * @param key
     * @return 不存在则返回默认值{@code false}
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public void put(String key, int value) {
        editor.putInt(key, value).apply();
    }

    /**
     * SP中读取int
     * @param key
     * @return 不存在则返回默认值-1
     */
    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public void put(String key, long value) {
        editor.putLong(key, value).apply();
    }

    /**
     *
     * @param key
     * @return 不存在则返回默认值-1
     */
    public long getLong(String key) {
        return getLong(key, -1L);
    }

    public long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public void put(String key, float value) {
        editor.putFloat(key, value).apply();
    }

    /**
     *
     * @param key
     * @return 不存在则返回默认值-1
     */
    public float getFloat(String key) {
        return getFloat(key, -1f);
    }

    public float getFloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    public void put(String key, String value) {
        editor.putString(key, value).apply();
    }

    /**
     *
     * @param key
     * @return 不存在则返回默认值{@code null}
     */
    public String getString(String key) {
        return getString(key, null);
    }


    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void put(String key, Set<String> value) {
        editor.putStringSet(key, value).apply();
    }

    /**
     *
     * @param key
     * @return 不存在则返回默认值{@code null}
     */
    public Set<String> getStringSet(String key) {
        return getStringSet(key, null);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    /**
     * SP中获取所有键值对
     * @return
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * SP中移除该key
     * @param key
     */
    public void remove(String key) {
        editor.remove(key).apply();
    }

    /**
     * SP中是否存在该key
     * @param key
     * @return
     */
    public boolean contains(String key) {
        return sp.contains(key);
    }

    /**
     * SP中清除所有数据
     */
    public void clear() {
        editor.clear().apply();
    }

    public interface SPInterface {
        public static final String KEY_DATA = "data";
        public static final String KEY_DEFAULT = KEY_DATA;
    }
}

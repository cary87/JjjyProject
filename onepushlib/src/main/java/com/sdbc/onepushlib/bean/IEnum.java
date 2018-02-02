package com.sdbc.onepushlib.bean;

/**
 * author:yangweiquan
 * create:2017/5/4
 */
public interface IEnum<T> {

    /**
     * 获取枚举值
     * @return
     */
    public T getValue();

    /**
     * 获取枚举的显示值
     * @return
     */
    public String getDisplay();
}

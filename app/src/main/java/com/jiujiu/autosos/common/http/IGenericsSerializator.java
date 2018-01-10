package com.jiujiu.autosos.common.http;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public interface IGenericsSerializator {
    <T> T transform(String response, Class<T> classOfT);
}

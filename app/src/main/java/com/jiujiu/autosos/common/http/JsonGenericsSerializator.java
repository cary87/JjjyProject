package com.jiujiu.autosos.common.http;

import com.google.gson.Gson;

/**
 * json解析
 */
public class JsonGenericsSerializator implements IGenericsSerializator {
    private static final String TAG = JsonGenericsSerializator.class.getName();

    Gson mGson = new Gson();
    @Override
    public <T> T transform(String response, Class<T> classOfT) {
        return mGson.fromJson(response, classOfT);
    }
}

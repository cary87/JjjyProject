package com.jiujiu.autosos.common.http;

import com.jiujiu.autosos.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * 请求回调基类
 */
@Deprecated
public abstract class BaseCallback<T> extends Callback<T> {

    public static final String TAG = BaseCallback.class.getSimpleName();

    IGenericsSerializator mGenericsSerializator;

    public BaseCallback() {
        mGenericsSerializator = new JsonGenericsSerializator();
    }

    public BaseCallback(IGenericsSerializator serializator) {
        mGenericsSerializator = serializator;
    }

    @Override
    public final T parseNetworkResponse(Response response, int id) throws Exception {
        String resp = response.body().string();
        LogUtils.i(TAG, resp);
        return parseResponse(resp);
    }

    public T parseResponse(String string) {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (entityClass == String.class) {
            return (T) string;
        }
        T bean = mGenericsSerializator.transform(string, entityClass);

        return bean;
    }
}

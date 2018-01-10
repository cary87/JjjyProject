package com.jiujiu.autosos.common.http;

import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;

import okhttp3.Response;

/**
 * Created by Cary on 2017/5/4 0004.
 */
public abstract class ApiCallback<T extends BaseResp> extends Callback<T> {
    public static final String TAG = ApiCallback.class.getSimpleName();

    IGenericsSerializator mGenericsSerializator;

    public ApiCallback() {
        mGenericsSerializator = new JsonGenericsSerializator();
    }

    public ApiCallback(IGenericsSerializator serializator) {
        mGenericsSerializator = serializator;
    }

    @Override
    public final T parseNetworkResponse(Response response, int id) throws Exception {
        String resp = response.body().string();
        LogUtils.i(TAG, resp);
        return parseResponse(resp);
    }

    public T parseResponse(String string) throws Exception {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        T bean = mGenericsSerializator.transform(string, entityClass);
        if (bean != null && !Constant.CODE_SUCCESS.equals(bean.getCode())) {
            throw new AppException(bean.getCode(), bean.getMessage());
        }
        return bean;
    }

}

package com.jiujiu.autosos.api;


import com.jiujiu.autosos.common.http.ApiHelper;
import com.jiujiu.autosos.common.http.HttpMethod;
import com.zhy.http.okhttp.callback.Callback;

/**
 * 推送配置相关的接口
 */
public class ConfigApi {
    public static final String PUSH_CONFIG = "/common/push/config";

    public static <T> void pushConfig(Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.GET, PUSH_CONFIG, null, callback);
    }

}

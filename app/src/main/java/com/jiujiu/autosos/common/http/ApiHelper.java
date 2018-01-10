package com.jiujiu.autosos.common.http;

import android.net.Uri;

import com.google.gson.Gson;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.common.utils.MapUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Api
 */
public class ApiHelper {
    private static final String TAG = ApiHelper.class.getSimpleName();

    public static void initOKHttpClient() {

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
                .connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .cookieJar(new CookieJarImpl(new MemoryCookieStore()))
                .hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        if (Constant.DEBUG) {
            okBuilder.addInterceptor(new LoggerInterceptor("TAG"));
        }
        OkHttpClient okHttpClient = okBuilder.build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 异步调用
     * @param method
     * @param url
     * @param param
     * @param callback
     * @param <T>
     */
    public static <T> void httpRequest(HttpMethod method, String url, Map<String,String> param, Callback<T> callback) {
        url = ApiConstants.ROOT_URL + url;
        if (UserStorage.getInstance().getToken() != null) {
            url = url + "?token=" + UserStorage.getInstance().getToken();
        }
        HashMap<String, String> platformParam = new HashMap<>();
        platformParam.put(Constant.KEY_APPID, Constant.APPID);
        platformParam.put(Constant.KEY_PLATFORM,Constant.PLATFORM);
        platformParam.put(Constant.KEY_VERSION, Constant.VERSION);
        platformParam.put(Constant.KEY_VERSIONCODE, Constant.VERSIONCODE);
        if(param == null) {
            param = platformParam;
        } else {
            param.putAll(platformParam);
        }
        param = MapUtils.removeNullValue(param);
        LogUtils.i(TAG, param.toString());
        switch (method) {
            case GET:
                GetBuilder getBuilder = new GetBuilder();
                getBuilder.params(param);
                getBuilder.url(url);
                getBuilder.build().execute(callback);
                break;
            case POST:
                PostFormBuilder postBuilder = new PostFormBuilder();
                postBuilder.params(param);
                postBuilder.url(url);
                postBuilder.build().execute(callback);
                break;
        }

    }

    /**
     * 同步调用
     * @param method
     * @param url
     * @param param
     * @param clz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T syncHttpRequest(HttpMethod method, String url, Map<String,String> param, Class<T> clz) throws Exception {
        url = ApiConstants.ROOT_URL + url;
        if (UserStorage.getInstance().getToken() != null) {
            url = url + "?token=" + UserStorage.getInstance().getToken();
        }
        HashMap<String, String> platformParam = new HashMap<>();
        platformParam.put(Constant.KEY_APPID, Constant.APPID);
        platformParam.put(Constant.KEY_PLATFORM,Constant.PLATFORM);
        platformParam.put(Constant.KEY_VERSION, Constant.VERSION);
        platformParam.put(Constant.KEY_VERSIONCODE, Constant.VERSIONCODE);
        if(param == null) {
            param = platformParam;
        } else {
            param.putAll(platformParam);
        }
        param = MapUtils.removeNullValue(param);
        LogUtils.i(TAG, param.toString());
        Response response = null;
        switch (method) {
            case GET:
                GetBuilder getBuilder = new GetBuilder();
                getBuilder.params(param);
                getBuilder.url(url);
                response = getBuilder.build().execute();
                break;
            case POST:
                PostFormBuilder postBuilder = new PostFormBuilder();
                postBuilder.params(param);
                postBuilder.url(url);
                response = postBuilder.build().execute();
                break;
        }

        if (response != null) {
            String responseStr = response.body().string();
            LogUtils.i("wzh", "请求返回：" + responseStr);
            return  new Gson().fromJson(responseStr, clz);
        }

        return null;
    }

    public static <T> void upload(Map<String,String> param, String url, File file, Callback<T> callback) {
        url = ApiConstants.ROOT_URL + url;
        HashMap<String, String> platformParam = new HashMap<>();
        platformParam.put(Constant.KEY_APPID, Constant.APPID);
        platformParam.put(Constant.KEY_PLATFORM,Constant.PLATFORM);
        platformParam.put(Constant.KEY_VERSION, Constant.VERSION);
        platformParam.put(Constant.KEY_VERSIONCODE, Constant.VERSIONCODE);

        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = platformParam.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext())
        {
            String key = iterator.next();
            builder.appendQueryParameter(key, platformParam.get(key));
        }
        LogUtils.i(TAG, platformParam.toString());
        PostFormBuilder postBuilder = new PostFormBuilder();
        postBuilder.params(param);
        postBuilder.url(builder.build().toString());
        postBuilder.addFile("file", file.getName(), file);
        postBuilder.build().execute(callback);

    }

}

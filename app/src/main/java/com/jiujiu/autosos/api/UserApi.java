package com.jiujiu.autosos.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.common.http.ApiHelper;
import com.jiujiu.autosos.common.http.HttpMethod;
import com.jiujiu.autosos.req.DriverInfoReq;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Cary on 2018/1/4 0018.
 */
public class UserApi {
    public static final String LOGIN = "/common/users/login";
    public static final String PUBLICKEY = "/common/publicKey";
    public static final String CHANGEPASSWORD = "/common/users/changePassword";
    public static final String SETONLINESTATE = "/common/users/setOnlineState";
    public static final String DRIVERINFOUPDATE = "/common/users/driverInfoUpdate";
    public static final String UPDATEPOSITION = "/common/position/updatePosition";
    public static final String SAVELICENSE = "/common/users/saveLicenseFile";
    public static final String ACCEPTORDER = "/common/order/driverAcceptOrder";


    public static final String VCCODE = "/common/sendVCode";
    public static final String FORGOTPASSWORD = "/common/users/forgotPassword";
    public static final String VERIFYMOBILE = "/common/verifyMobile";
    public static final String CHECKMOBILE = "/common/users/checkMobile";
    public static final String UPLOAD = "/common/file/upload";
    public static final String LOGOUT = "/common/users/logout";
    public static final String VERIFYEMAIL = "/common/users/validateEmail";

    public static <T> T login(Map<String,String> param, Class<T> clz) throws Exception {
        return ApiHelper.syncHttpRequest(HttpMethod.GET, LOGIN, param, clz);
    }

    public static <T> T getPublicKey(Class<T> clz) throws Exception {
        return ApiHelper.syncHttpRequest(HttpMethod.GET, PUBLICKEY, null, clz);
    }

    public static <T> T changePwd(Map<String,String> param, Class<T> clz) throws Exception {
        return ApiHelper.syncHttpRequest(HttpMethod.POST, CHANGEPASSWORD, param, clz);
    }

    public static <T> T driverInfoUpdate(DriverInfoReq req, Class<T> clz) throws Exception {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String json = gson.toJson(req);
        Map<String,String> param = gson.fromJson(json, new TypeToken<Map<String, String>>() {}.getType());
        return ApiHelper.syncHttpRequest(HttpMethod.POST, DRIVERINFOUPDATE, param, clz);
    }

    public static <T> T setOnlineState(Map<String,String> param, Class<T> clz) throws Exception {
        return ApiHelper.syncHttpRequest(HttpMethod.POST, SETONLINESTATE, param, clz);
    }

    public static <T> T updatePosition(Map<String,String> param, Class<T> clz) throws Exception {
        return ApiHelper.syncHttpRequest(HttpMethod.POST, UPDATEPOSITION, param, clz);
    }

    public static <T> void saveLicense(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, SAVELICENSE, param, callback);
    }

    public static <T> void driverAcceptOrder(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, ACCEPTORDER, param, callback);
    }


    public static <T> void sendVCCode(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, VCCODE, param, callback);
    }

    public static <T> void verifyMobile(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, VERIFYMOBILE, param, callback);
    }

    public static <T> void forgotPassword(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, FORGOTPASSWORD, param, callback);
    }

    public static <T> void upload(Map<String,String> param, File file, Callback<T> callback) {
        ApiHelper.upload(param, UPLOAD, file, callback);
    }

    public static <T> void logout(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, LOGOUT, param, callback);
    }

    /**
     *  邮箱验证
     * @param email
     * @param callback
     * @param <T>
     */
    public static <T> void verifyEmail(String email, Callback<T> callback) {
        Map<String, String> param = new HashMap<>();
        param.put("email", email);
        ApiHelper.httpRequest(HttpMethod.POST, VERIFYEMAIL, param, callback);
    }

}

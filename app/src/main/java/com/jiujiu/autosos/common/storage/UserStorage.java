package com.jiujiu.autosos.common.storage;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jiujiu.autosos.common.AutososApplication;
import com.jiujiu.autosos.resp.UserResp;
import com.litesuits.common.data.DataKeeper;

/**
 * Created by Administrator on 2018/1/4.
 */

public class UserStorage {
    public static final String SHAREPRE = "app_save";
    public static final String KEY_USER = "key_user";
    private static final String KEY_LAST_LONGITUDE = "key_last_longitude";
    private static final String KEY_LAST_LATITUDE = "key_last_latitude";
    private static UserStorage instance;
    private String token;
    private UserResp.DataBean user;
    private String nowLocationAddress;

    private UserStorage() {
        try {
            DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
            String loginRespJson = keeper.get(KEY_USER, "");
            if (!TextUtils.isEmpty(loginRespJson)) {
                user = new Gson().fromJson(loginRespJson, UserResp.DataBean.class);
                token = user.getToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserStorage getInstance() {
        if (instance == null) {
            synchronized (UserStorage.class) {
                if (instance == null) {
                    instance = new UserStorage();
                }
            }
        }
        return instance;
    }

    public void updateServiceInfoSetted() {
        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        if (user != null) {
            keeper.put(user.getUserId(), true);
        }
    }

    public boolean isServiceInfoSetted() {
        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        if (user != null) {
            return keeper.get(user.getUserId(), false);
        }
        return true;
    }

    public boolean isLogined() {
        return token != null;
    }

    public boolean isOnline() {
        return user.getOnlineState() == 0;
    }

    public String getNowLocationAddress() {
        return nowLocationAddress;
    }

    public void setNowLocationAddress(String nowLocationAddress) {
        this.nowLocationAddress = nowLocationAddress;
    }

    public double getLastSubmitLongitude() {
        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        return Double.parseDouble(keeper.get(KEY_LAST_LONGITUDE + (user == null ? "" : user.getUserId()), "0"));
    }

    public void setLastSubmitLongitude(double lastSubmitLongitude) {
        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        keeper.put(KEY_LAST_LONGITUDE + (user == null ? "" : user.getUserId()), Double.toString(lastSubmitLongitude));
    }

    public double getLastSubmitLatitude() {
        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        return Double.parseDouble(keeper.get(KEY_LAST_LATITUDE + (user == null ? "" : user.getUserId()), "0"));
    }

    public void setLastSubmitLatitude(double lastSubmitLatitude) {
        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        keeper.put(KEY_LAST_LATITUDE + (user == null ? "" : user.getUserId()), Double.toString(lastSubmitLatitude));
    }

    public String getToken() {
        return token;
    }

    public void updateToken(String token) {
        this.token = token;
        this.user.setToken(token);

        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        keeper.put(KEY_USER, new Gson().toJson(user));
    }

    public UserResp.DataBean getUser() {
        return user;
    }

    public void setUser(UserResp.DataBean user) {
        this.user = user;
        this.token = user.getToken();

        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        keeper.put(KEY_USER, new Gson().toJson(user));
    }

    public void clear() {
        token = null;
        user = null;
        DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
        keeper.put(KEY_USER, null);
    }
}

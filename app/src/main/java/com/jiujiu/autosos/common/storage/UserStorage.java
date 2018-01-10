package com.jiujiu.autosos.common.storage;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jiujiu.autosos.common.AutososApplication;
import com.jiujiu.autosos.resp.LoginResp;
import com.litesuits.common.data.DataKeeper;

/**
 * Created by Administrator on 2018/1/4.
 */

public class UserStorage {
    public static final String SHAREPRE = "app_save";
    public static final String KEY_USER = "key_user";
    private static UserStorage instance;
    private String token;
    private LoginResp.DataBean user;
    private Double oldlongitude;
    private Double oldlatitude;

    private UserStorage() {
        try {
            DataKeeper keeper = new DataKeeper(AutososApplication.getApp(), SHAREPRE);
            String loginRespJson = keeper.get(KEY_USER, "");
            if (!TextUtils.isEmpty(loginRespJson)) {
                user = new Gson().fromJson(loginRespJson, LoginResp.DataBean.class);
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

    public Double getOldlongitude() {
        return oldlongitude;
    }

    public void setOldlongitude(Double oldlongitude) {
        this.oldlongitude = oldlongitude;
    }

    public Double getOldlatitude() {
        return oldlatitude;
    }

    public void setOldlatitude(Double oldlatitude) {
        this.oldlatitude = oldlatitude;
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

    public LoginResp.DataBean getUser() {
        return user;
    }

    public void setUser(LoginResp.DataBean user) {
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

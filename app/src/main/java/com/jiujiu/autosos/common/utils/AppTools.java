package com.jiujiu.autosos.common.utils;

import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.nav.LocationManeger;
import com.sdbc.onepushlib.OnePush;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/2/23.
 */

public class AppTools {
    private AppTools() {

    }

    public static void logout(AbsBaseActivity activity) {
        UserApi.logout(null, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(BaseResp baseResp, int i) {

            }
        });
        OnePush.logout(activity);
        UserStorage.getInstance().clear();
        LocationManeger.getInstance().stopLocation();
        activity.finish();
    }
}

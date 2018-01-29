package com.jiujiu.autosos.common.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.jiujiu.autosos.api.ConfigApi;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.push.OnePushReceiver;
import com.jiujiu.autosos.resp.PushConfigResp;
import com.sdbc.onepushlib.OnePush;
import com.sdbc.onepushlib.bean.AppTypeEnum;
import com.sdbc.onepushlib.utils.StringUtils;

import okhttp3.Call;

/**
 * 推送工具类
 */
public class PushUtils {

    /**
     * 初始化推送
     */
    public static void initPush(final Context context) {
        if (!OnePush.isInitialized()) {
            ConfigApi.pushConfig(new ApiCallback<PushConfigResp>() {
                @Override
                public void onError(Call call, Exception e, int i) {
                }

                @Override
                public void onResponse(PushConfigResp pushConfigResp, int i) {
                    if (pushConfigResp != null) {
                        PushConfigResp.PushConfig pushConfig = pushConfigResp.getData();
                        if (pushConfig != null && StringUtils.isNotBlank(pushConfig.getServerIP())
                                && pushConfig.getServerPort() > 0) {
                            try {
                                String userId = UserStorage.getInstance().getUser().getUserId();
                                String userIdStr = String.valueOf(userId);
                                OnePush.setDebug(Constant.DEBUG);
                                OnePush.init(context, AppTypeEnum.DOCTOR,
                                        userIdStr, pushConfig.getServerIP(), pushConfig.getServerPort(),
                                        pushConfig.isForceMIPush());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    public static Intent intentHandled = null;


    /**
     * 处理推送
     * @param context
     * @param intentFrom
     */
    public static void handlePush(Context context, Intent intentFrom) {
        if (context == null) {
            return;
        }
        // 关闭通知
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        if (intentFrom == null) {
            return;
        }
        if (intentHandled == intentFrom) {
            // 已经处理过
            return;
        }
        intentHandled = intentFrom;

        OrderModel order = (OrderModel) intentFrom.getSerializableExtra(OnePushReceiver.KEY_ORDER);
        if (order == null) {
            return;
        }
    }

}

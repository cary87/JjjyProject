package com.sdbc.onepushlib;

import android.content.Context;
import android.content.Intent;

import com.sdbc.onepushlib.bean.AppTypeEnum;
import com.sdbc.onepushlib.bean.Config;
import com.sdbc.onepushlib.bean.OsTypeEnum;
import com.sdbc.onepushlib.exception.PushException;
import com.sdbc.onepushlib.net.udp.KeepAliveDaemon;
import com.sdbc.onepushlib.utils.DeviceUtils;
import com.sdbc.onepushlib.utils.PushLogs;
import com.sdbc.onepushlib.utils.StringUtils;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * 推送客户端类
 * author:yangweiquan
 * create:2017/5/4
 */
public class OnePush {

    private static Context mContext;
    /**
     * OnePush是否初始化
     */
    private static boolean mIsInit = false;

    /**
     * 初始化推送
     * @param context
     * @param appTypeEnum 分配给APP的AppTypeEnum
     * @param userId 系统中的用户id
     * @param serverIP 推送服务端IP
     * @param serverPort 推送服务端端口
     * @param forceMIPush 是否强制使用小米推送
     * @throws PushException
     */
    public static void init(Context context, AppTypeEnum appTypeEnum, String userId, String serverIP, int serverPort, boolean forceMIPush) throws PushException {
        if (!mIsInit) {
            if (context == null) {
                throw new PushException("context == null");
            }
            mContext = context.getApplicationContext();

            if (appTypeEnum == null) {
                throw new PushException("appTypeEnum不能为空");
            }
            if (StringUtils.isEmpty(userId)) {
                throw new PushException("userId不能为空");
            }
            if (StringUtils.isEmpty(serverIP)) {
                throw new PushException("serverIP不能为空");
            }
            if (serverPort <= 0) {
                throw new PushException("serverPort必须大于零");
            }
            Config.getInstance()
                    .setAppType(appTypeEnum.getAppType())
                    .setUserId(userId)
                    .setServerIP(serverIP)
                    .setServerPort(serverPort);
            if (forceMIPush || DeviceUtils.isMIUI(context)) {
                // 是小米系统
                MiPushClient.registerPush(context, appTypeEnum.getMiAppId(), appTypeEnum.getMiAppKey());
                String regId = MiPushClient.getRegId(context);
                PushLogs.d("wzh", "regId:" + regId);
                if (StringUtils.isNotBlank(regId)) {
                    Config.getInstance().setToken(regId);
                    Config.getInstance().setOsType(OsTypeEnum.MIA);
                    // 发送短的心跳包
                    KeepAliveDaemon.getInstance(context).startShort(true);
                    return;
                }
            }

            // 配置是否正确来判断是否初始化成功
            mIsInit = Config.getInstance().isConfigOk();
            if (mIsInit) {
                // 保存成功初始化的配置
                Config.getInstance().save2Local(context);
                // 启动推送服务
                context.startService(new Intent(context, OnePushService.class));
            }
        }
    }

    /**
     * 设置用户ID
     * @param userId
     * @return 只有userId不为空的情况下才能设置成功
     */
//    public static boolean setUserId(String userId) {
//        if (!StringUtils.isEmpty(userId)) {
//            Config.getInstance().setUserId(userId);
//            // 比较一下该userId是否与本地的userId一致
//            Config localConfig = Config.getFromLocal(mContext);
//            if (localConfig != null && !userId.equals(localConfig.getUserId())) {
//                localConfig.setUserId(userId);
//                localConfig.save2Local(mContext);
//            }
//            return true;
//        }
//
//        return false;
//    }

    /**
     * 设置是否调试（默认关闭调试）
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        Config.getInstance().setDebug(isDebug);
    }

    public static boolean isInitialized() {
        return mIsInit;
    }

    public static void logout(Context context) {
        // 用户退出，清空本地配置
        Config.getInstance().clearLocal(context);
        mIsInit = false;
        // 关闭服务
        Intent service = new Intent(context, OnePushService.class);
        context.stopService(service);
    }
}

package com.sdbc.onepushlib;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sdbc.onepushlib.bean.Config;
import com.sdbc.onepushlib.net.udp.KeepAliveDaemon;
import com.sdbc.onepushlib.net.udp.LocalUDPDataReciever;
import com.sdbc.onepushlib.net.udp.LocalUDPSocketProvider;
import com.sdbc.onepushlib.service.OnePushFakeService;

/**
 * 后台运行数据包接收线程、心跳包发送线程的Service
 * author:yangweiquan
 * create:2017/5/11
 */
public class OnePushService extends Service {

    /**
     * 服务延迟启动时间
     */
    private int SERVICE_START_DELAYED = 5;
    /**
     * 本地网络是否ok
     */
    private static boolean mLocalNetworkOk = true;


    @Override
    public void onCreate() {
        super.onCreate();
        // 监听网络变化
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkConnectionStatusBroadcastReceiver, intentFilter);

        cancelAutoStartService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 从本地加载配置
        Config.getInstance().loadFromLocal(this);
        if (Config.getInstance().isConfigOk()) {
            start(this);
            OnePushFakeService.startForeground(this);
            flags = START_STICKY;
            SERVICE_START_DELAYED = 5;
            return super.onStartCommand(intent, flags, startId);
        } else {
            int ret = super.onStartCommand(intent,flags,startId);
            release(this);
            stopSelf();
            SERVICE_START_DELAYED += SERVICE_START_DELAYED;
            return ret;
        }
    }

    /**
     * OnePushService调用
     */
    private void start(Context context) {
        if (Config.getInstance().isOtherSystem()) {
            // 启动本地UDP监听
            LocalUDPDataReciever.getInstance(context).startup();
            // 启动心跳数据包发送服务
            KeepAliveDaemon.getInstance(context).start(true);
        } else {
            // 发送短的心跳包
            KeepAliveDaemon.getInstance(context).startShort(true);
        }
    }

    private void cancelAutoStartService(Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(getOperation(context));
    }

    private PendingIntent getOperation(Context context) {
        Intent intent = new Intent(context, OnePushService.class);
        PendingIntent operation = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return operation;
    }

    @Override
    public void onDestroy() {
        release(this);
        super.onDestroy();
        startServiceAfterClosed(this, SERVICE_START_DELAYED);
        unregisterReceiver(mNetworkConnectionStatusBroadcastReceiver);
    }

    private void release(Context context) {
        // 停止本地UDP监听
        LocalUDPDataReciever.getInstance(context).stop();
        // 停止心跳数据包发送服务
        KeepAliveDaemon.getInstance(context).stop();
    }

    /**
     * service停掉后自动启动应用
     * @param context
     * @param delayed 延后启动的时间，单位为秒
     */
    private void startServiceAfterClosed(Context context, int delayed) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayed * 1000,
                getOperation(context));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static final BroadcastReceiver mNetworkConnectionStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 网络变化，关闭本地的UDPSocket
            LocalUDPSocketProvider.getInstance().closeLocalUDPSocket();
            // 获取当前网络状态
            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI
                        && networkInfo.isConnected()) {
                    // wifi网络
                    networkOk();
                    return;
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE
                        && networkInfo.isConnected()) {
                    // 手机网络
                    networkOk();
                    return;
                }
            }
            // 没有网络连接
            mLocalNetworkOk = false;
        }
    };

    private static void networkOk() {
        mLocalNetworkOk = true;
    }

    public static boolean isLocalNetworkOk() {
        return mLocalNetworkOk;
    }

}

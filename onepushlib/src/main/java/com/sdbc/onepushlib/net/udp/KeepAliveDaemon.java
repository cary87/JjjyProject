package com.sdbc.onepushlib.net.udp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.sdbc.onepushlib.bean.Config;
import com.sdbc.onepushlib.bean.ErrorCodeEnum;
import com.sdbc.onepushlib.utils.PushLogs;

/**
 * 心跳发送线程
 * author:yangweiquan
 * create:2017/5/11
 */
public class KeepAliveDaemon {

    private static final String TAG = KeepAliveDaemon.class.getName();
    private static KeepAliveDaemon mInstance = null;

    private Context mContext;
    private Handler mHandler = null;
    private Runnable mRunnable = null;

    /**
     * 是否正在执行
     */
    private boolean mIsExcuting = false;
    /**
     * 上一次成功发送心跳包时间戳
     */
    private long lastSendHeartBeatDataTimestamp = 0L;
    private int keepAliveSendCount = 0;
    /**
     * 是否为短的心跳包发送服务
     */
    private boolean keepAliveShort = false;
    private boolean keepAliveRunning = false;

    public static KeepAliveDaemon getInstance(Context context) {
        if (mInstance == null) {
            synchronized (KeepAliveDaemon.class) {
                if (mInstance == null) {
                    mInstance = new KeepAliveDaemon(context);
                }
            }
        }

        return mInstance;
    }

    private KeepAliveDaemon(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        this.mHandler = new Handler();
        this.mRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (KeepAliveDaemon.class) {
                    if (mIsExcuting) {
                        // 如果正在执行，则结束此处线程
                        return;
                    }
                }
                new AsyncTask<Void, Void, Long>() {

                    @Override
                    protected Long doInBackground(Void... voids) {
                        mIsExcuting = true;
                        try {
                            PushLogs.d(TAG, "心跳线程执行中...");
                            ErrorCodeEnum code = LocalUDPDataSender.getInstance(KeepAliveDaemon.this.mContext).sendKeepAlive();
                            if (code == ErrorCodeEnum.OK) {
                                PushLogs.d(TAG, "心跳包发送成功。");
                                lastSendHeartBeatDataTimestamp = System.currentTimeMillis();
                                keepAliveSendCount ++;
                                if (keepAliveShort
                                        && keepAliveSendCount >= Config.getInstance().getShortHeartBeatMaxCount()) {
                                    return -1L;
                                }
                                return Config.getInstance().getKeepAliveInterval();
                            } else {
                                PushLogs.d(TAG, "心跳包发送失败，code:" + code.getValue() + ", " + code.getDisplay());
                                return Config.getInstance().getKeepAliveIntervalError();
                            }
                        } catch (Exception e) {
                            PushLogs.e(TAG, "发送心跳包出错，原因：" + e.getMessage(), e);
                            return Config.getInstance().getKeepAliveIntervalError();
                        } finally {
                            mIsExcuting = false;
                        }
                    }

                    @Override
                    protected void onPostExecute(Long intervalTime) {
                        if (intervalTime >= 0) {
                            KeepAliveDaemon.this.mHandler.postDelayed(KeepAliveDaemon.this.mRunnable,
                                    intervalTime);
                        }
                    }
                }.execute();
            }
        };
    }

    public void stop() {
        this.mHandler.removeCallbacks(this.mRunnable);
        this.keepAliveRunning = false;
        this.lastSendHeartBeatDataTimestamp = 0L;
    }

    public void start(boolean immediately) {
        stop();
        keepAliveShort = false;
        start(immediately ? 0 : Config.getInstance().getKeepAliveInterval());
    }

    /**
     * 发送5次心跳包后自动停止
     * @param immediately
     */
    public void startShort(boolean immediately) {
        stop();
        keepAliveShort = true;
        start(immediately ? 0 : Config.getInstance().getKeepAliveInterval());
    }

    private void start(long intervalTime) {
        this.mHandler.postDelayed(this.mRunnable, intervalTime);
        this.keepAliveRunning = true;
    }

    public boolean isKeepAliveRunning() {
        return keepAliveRunning;
    }
}

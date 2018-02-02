package com.sdbc.onepushlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * author:yangweiquan
 * create:2017/5/11
 */
public abstract class OnePushAbsReceiver extends BroadcastReceiver {

    public static final String ACTION_MESSAGE_RECEIVE = "com.android.onepush.message_receive";
    public static final String KEY_CONTENT = "content";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_MESSAGE_RECEIVE.equals(intent.getAction())) {
            // 收到透传消息
            String content = intent.getStringExtra(KEY_CONTENT);
            onReceivePassThrounghMessage(context, content);
        }
    }

    /**
     * 接收到透传消息
     * @param context
     * @param content 透传消息的内容，一般为json格式
     */
    public abstract void onReceivePassThrounghMessage(Context context, String content);
}

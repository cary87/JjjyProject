package com.sdbc.onepushlib.xiaomi;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sdbc.onepushlib.OnePushAbsReceiver;
import com.sdbc.onepushlib.utils.PushLogs;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * 小米推送接收器
 * @author yangweiquan
 * @date 2017-06-12
 */
public class XiaoMiMessageReceiver extends PushMessageReceiver {

    public static final String TAG = XiaoMiMessageReceiver.class.getName();

    /**
     * 透传消息
     * @param context
     * @param miPushMessage
     */
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        super.onReceivePassThroughMessage(context, miPushMessage);
        String message = miPushMessage.getContent();
        PushLogs.d(TAG, "小米推送收到内容：" + message);
        if (!TextUtils.isEmpty(message)) {
            Intent broadcastData = new Intent(OnePushAbsReceiver.ACTION_MESSAGE_RECEIVE);
            broadcastData.putExtra(OnePushAbsReceiver.KEY_CONTENT, message.trim());
            context.sendBroadcast(broadcastData);
        }
    }
}

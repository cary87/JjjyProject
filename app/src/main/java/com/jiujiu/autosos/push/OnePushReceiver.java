package com.jiujiu.autosos.push;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.PushOrderEvent;
import com.sdbc.onepushlib.OnePushAbsReceiver;
import com.sdbc.onepushlib.bean.MessageBody;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 消息接收处理
 */
public class OnePushReceiver extends OnePushAbsReceiver {

    public static final String KEY_ORDER = "key_order";

    // notificationId
    private static int notificationId = 170609100;

    @Override
    public void onReceivePassThrounghMessage(final Context context, final String content) {
        LogUtils.i("wzh", content);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (!content.startsWith("{") || !content.endsWith("}")) {
            return;
        }

        Single.fromCallable(new Callable<OrderModel>() {
            @Override
            public OrderModel call() throws Exception {
                MessageBody messageBody = new Gson().fromJson(content, MessageBody.class);
                if (messageBody == null || TextUtils.isEmpty(messageBody.getTo()) || !messageBody.getTo().equals(UserStorage.getInstance().getUser().getUserId())) {
                    return null;
                }
                //解析MessageBody-content
                String realContent = new String(Base64.decode(messageBody.getContent(), Base64.DEFAULT));
                LogUtils.i("wzh", "---decoded push content---:" + realContent);
                OrderModel order = new Gson().fromJson(realContent, OrderModel.class);
                List<OrderItem> orderItems = new Gson().fromJson(order.getItems(), new TypeToken<List<OrderItem>>() {}.getType());
                order.setOrderItems(orderItems);
                return order;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<OrderModel>() {
                    @Override
                    public void accept(OrderModel order) throws Exception {
                        if (order != null) {
                            showNotification(context, order, notificationId++);
                            EventBus.getDefault().post(new PushOrderEvent(order));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e("wzh", throwable.toString());
                    }
                });
    }

    /**
     * 显示通知
     * @param context
     * @param order
     * @param notificationId
     */
    private void showNotification(Context context, OrderModel order, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        Intent intentLaunch = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intentLaunch.putExtra(KEY_ORDER, order);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, intentLaunch,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("新订单")
                .setContentText("救援订单, 地址：" + order.getAddress() + ", 手机：" + order.getCarOwnerId())
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .build();
        notificationManager.notify(notificationId, notification);
    }

}

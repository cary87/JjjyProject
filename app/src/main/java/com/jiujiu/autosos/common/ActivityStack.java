package com.jiujiu.autosos.common;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;

import java.util.Stack;

/**
 * Activity管理类
 */
public class ActivityStack {

    private static Stack<Activity> mActivityStack;//视图栈
    private static ActivityStack mInstance;//AppManager对象

    /**
     * 构造方法私有化
     */
    private ActivityStack() {}

    /**
     * 获取管理类对象
     *
     * @return
     */
    public static ActivityStack getInstance () {
        if (mInstance == null) {
            synchronized (ActivityStack.class) {
                mInstance = new ActivityStack();
            }
        }
        return mInstance;
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束视图盏下所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void exitApp(Context context) {
        try {
            NotificationManager mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.cancelAll();
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {

        }
    }

}

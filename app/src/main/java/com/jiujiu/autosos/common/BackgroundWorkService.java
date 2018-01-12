package com.jiujiu.autosos.common;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.nav.LocationManeger;
import com.xdandroid.hellodaemon.AbsWorkService;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public class BackgroundWorkService extends AbsWorkService {
    public static boolean mShouldStopService;

    private LocationManeger mLocationManeger;

    public static void stopService() {
        mShouldStopService = true;
        cancelJobAlarmSub();
    }

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return mShouldStopService;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        LogUtils.i("wzh", "startWork");
        if (mLocationManeger == null) {
            mLocationManeger = LocationManeger.getInstance();
        }
        mLocationManeger.startLocation();
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {

    }
}

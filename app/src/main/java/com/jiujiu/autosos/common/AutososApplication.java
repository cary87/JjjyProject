package com.jiujiu.autosos.common;

import android.support.multidex.MultiDexApplication;

import com.jiujiu.autosos.common.http.ApiHelper;
import com.xdandroid.hellodaemon.DaemonEnv;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class AutososApplication extends MultiDexApplication {
    private static AutososApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //AMapNavi.setApiKey(this, "2de42711b03c9ac4b3b6bb0e9b601244");
        ApiHelper.initOKHttpClient();
        LitePal.initialize(this);

        DaemonEnv.initialize(this, BackgroundWorkService.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        BackgroundWorkService.mShouldStopService = false;
        DaemonEnv.startServiceMayBind(BackgroundWorkService.class);

    }

    public static AutososApplication getApp() {
        return mInstance;
    }
}

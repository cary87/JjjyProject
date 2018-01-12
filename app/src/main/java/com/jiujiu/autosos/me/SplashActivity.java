package com.jiujiu.autosos.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jaeger.library.StatusBarUtil;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.home.MainActivity;


/**
 * Created by Cary on 2017/4/25 0025.
 */
public class SplashActivity extends AbsBaseActivity {

    @Override
    protected void setup(Bundle savedInstanceState) {
        StatusBarUtil.setTransparent(this);
        jump();

    }

    private void jump() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundleLaunch = null;
                Intent intentLaunch = getIntent();
                if (intentLaunch != null) {
                    bundleLaunch = intentLaunch.getExtras();
                }
                Class clz;
                if (UserStorage.getInstance().getToken() != null) {
                    clz = MainActivity.class;
                } else {
                    clz = LoginActivity.class;
                }
                Intent intent = new Intent(SplashActivity.this, clz);
                if (bundleLaunch != null) {
                    intent.putExtras(bundleLaunch);
                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }
}

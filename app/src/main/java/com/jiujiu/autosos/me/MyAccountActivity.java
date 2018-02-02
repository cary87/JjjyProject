package com.jiujiu.autosos.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/30.
 */

public class MyAccountActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setupToolbar(toolbar);
        tvTitle.setText("我的账户");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_account;
    }

    public static void luanchSelf(Activity activity) {
        Intent intent = new Intent(activity, MyAccountActivity.class);
        activity.startActivity(intent);
    }

    @OnClick(R.id.ll_depost)
    public void onViewClicked() {
        WithDrawActivity.luanchSelf(mActivity);
    }
}

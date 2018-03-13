package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/9.
 */

public class UnNeedPayActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("完成订单");
        setupToolbar(toolbar);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_unneed_pay;
    }

    @Override
    public void onBackPressed() {
    }

    @OnClick(R.id.btn_end_order)
    public void onViewClicked() {
        //TODO 结束掉订单接口
        showLoadingDialog("订单完成中");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingDialog();
                showToast("订单已完成");
                finish();
            }
        }, 2000);
    }
}

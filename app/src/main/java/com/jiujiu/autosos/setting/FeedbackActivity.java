package com.jiujiu.autosos.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class FeedbackActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_feedback)
    TextInputEditText etFeedback;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("意见反馈");
        setupToolbar(toolbar);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_feedback;
    }

    public static void luanchSelf(Activity activity) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        activity.startActivity(intent);
    }

    @OnClick({R.id.btn_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_feedback:
                break;
        }
    }

}

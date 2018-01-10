package com.jiujiu.autosos.setting;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class AboutAcitvity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected void setup() {
        setupToolbar(toolbar);
        tvTitle.setText("关于");

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about;
    }


    public static void luanchSelf(Activity activity) {
        Intent intent = new Intent(activity, AboutAcitvity.class);
        activity.startActivity(intent);
    }
}

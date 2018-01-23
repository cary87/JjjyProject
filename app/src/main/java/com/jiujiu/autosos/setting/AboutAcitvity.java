package com.jiujiu.autosos.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class AboutAcitvity extends AbsBaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
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

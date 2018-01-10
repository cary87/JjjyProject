package com.jiujiu.autosos.camera;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/3.
 */

public class DisplayPictureTakenActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_display)
    ImageView ivDisplay;

    @Override
    protected void setup() {
        tvTitle.setText("拍照结果");
        String url = getIntent().getStringExtra("url");
        if (url != null) {
            Glide.with(this).load(url).into(ivDisplay);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = intent.getStringExtra("url");
        if (url != null) {
            Glide.with(this).load(url).into(ivDisplay);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_display_picture;
    }

    @OnClick({R.id.btn_retake, R.id.btn_finish_take, R.id.btn_take_one_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_retake:
                Intent reIntent = new Intent(this, CameraActivity.class);
                startActivity(reIntent);
                break;
            case R.id.btn_finish_take:
                finish();
                break;
            case R.id.btn_take_one_more:
                Intent moreIntent = new Intent(this, CameraActivity.class);
                startActivity(moreIntent);
                break;
        }
    }
}

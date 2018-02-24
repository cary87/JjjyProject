package com.jiujiu.autosos.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.order.model.TakePhotoEvent;
import com.jiujiu.autosos.resp.FileUploadResp;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.jiujiu.autosos.order.TakePhotoConstant.PHOTO_TAG;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_ARRIVE_TAKE;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_MOVE_UP_CAR_TAKE;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_TO_DES_TAKE;

/**
 * Created by Administrator on 2018/1/3.
 */

public class DisplayPictureTakenActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_display)
    ImageView ivDisplay;

    @BindView(R.id.pb_upload)
    NumberProgressBar pbUpload;

    /**
     * 哪种业务拍照
     */
    private int mTag = -1;

    /**
     * 图片上传后返回的相对路径
     */
    private List<String> paths = new ArrayList<>();

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("拍照结果");
        mTag = getIntent().getIntExtra(PHOTO_TAG, -1);

        String url = getIntent().getStringExtra("url");
        displayAndUpload(url);

    }

    /**
     * 上传拍摄的图片
     * @param file
     */
    private void uploadPicture(File file) {
        pbUpload.setProgress(0);
        pbUpload.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("key", "attach");
        showLoadingDialog("上传中");
        UserApi.upload(params, file, new ApiCallback<FileUploadResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                hideLoadingDialog();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                pbUpload.setProgress((int) progress *100);
                LogUtils.i("wzh", progress + "");

            }

            @Override
            public void onResponse(FileUploadResp resp, int i) {
                LogUtils.i("wzh", resp.toString());
                hideLoadingDialog();
                paths.add(resp.getData().getPath());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTag = getIntent().getIntExtra(PHOTO_TAG, -1);

        String url = intent.getStringExtra("url");
        displayAndUpload(url);
    }

    private void displayAndUpload(String url) {
        File file = new File(url);
        if (url != null && file.exists()) {
            Glide.with(this).load(url).into(ivDisplay);
            uploadPicture(file);
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
                if (mTag == TAG_ARRIVE_TAKE) {
                    EventBus.getDefault().post(new TakePhotoEvent(TAG_ARRIVE_TAKE, paths));
                }else if (mTag == TAG_MOVE_UP_CAR_TAKE) {
                    EventBus.getDefault().post(new TakePhotoEvent(TAG_MOVE_UP_CAR_TAKE, paths));
                } else if (mTag == TAG_TO_DES_TAKE) {
                    EventBus.getDefault().post(new TakePhotoEvent(TAG_TO_DES_TAKE, paths));
                } else {
                    EventBus.getDefault().post(new TakePhotoEvent(-1, paths));
                }
                finish();
                break;
            case R.id.btn_take_one_more:
                Intent moreIntent = new Intent(this, CameraActivity.class);
                startActivity(moreIntent);
                break;
        }
    }
}

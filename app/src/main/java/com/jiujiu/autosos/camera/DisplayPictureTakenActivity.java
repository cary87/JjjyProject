package com.jiujiu.autosos.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.order.model.PictureTypeEnum;
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

import static com.jiujiu.autosos.order.TakePhotoConstant.MAX_SURVERY_PICS_NUM;
import static com.jiujiu.autosos.order.TakePhotoConstant.PHOTO_INDEX;
import static com.jiujiu.autosos.order.TakePhotoConstant.PHOTO_TAG;


/**
 * Created by Administrator on 2018/1/3.
 */

public class DisplayPictureTakenActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_display)
    ImageView ivDisplay;

    /**
     * 哪种业务拍照
     */
    private PictureTypeEnum mTag = PictureTypeEnum.arrive;

    /**
     * 拍照完成返回的图片绝对路径
     */
    private List<String> localPaths = new ArrayList<>();

    /**
     * 图片上传后返回的相对路径
     */
    private List<String> paths = new ArrayList<>();

    /**
     * 要拍摄的第几张代查勘照片
     */
    private int picIndexOfSurvery = 1;

    private int multiUploadSize = 1;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("拍照结果");
        PictureTypeEnum pictureTypeEnum = (PictureTypeEnum) getIntent().getSerializableExtra(PHOTO_TAG);
        if (pictureTypeEnum != null) {
            mTag = pictureTypeEnum;
        }

        String url = getIntent().getStringExtra("url");
        display(url);

    }

    /**
     * 上传多张拍摄的图片,没有一次传多张接口，暂时这样解决
     *
     * @param urls
     */
    private void uploadMultiPictures(List<String> urls) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("上传中")
                .cancelable(false)
                .progress(true, 100).build();
        progressDialog.show();
        multiUploadSize = urls.size();
        for (String url : urls) {
            File file = new File(url);
            HashMap<String, String> params = new HashMap<>();
            params.put("key", "attach");
            UserApi.upload(params, file, new ApiCallback<FileUploadResp>() {
                @Override
                public void onError(Call call, Exception e, int i) {
                    showToast("上传失败");
                    progressDialog.dismiss();
                }

                @Override
                public void inProgress(float progress, long total, int id) {
                    super.inProgress(progress, total, id);
                    LogUtils.i("wzh", progress + "");
                }

                @Override
                public void onResponse(FileUploadResp resp, int i) {
                    LogUtils.i("wzh", resp.toString());
                    paths.add(resp.getData().getPath());
                    multiUploadSize--;
                    if (multiUploadSize == 0) {
                        progressDialog.dismiss();
                        handleUploadResult();
                    }
                }
            });
        }


    }

    /**
     * 处理上传完成后的逻辑
     */
    public void handleUploadResult() {
        switch (mTag) {
            case survery:
                if (picIndexOfSurvery < MAX_SURVERY_PICS_NUM) {
                    Intent nextTake = new Intent(mActivity, CameraActivity.class);
                    nextTake.putExtra(PHOTO_TAG, mTag);
                    nextTake.putExtra(PHOTO_INDEX, ++picIndexOfSurvery);
                    startActivity(nextTake);
                } else {
                    EventBus.getDefault().post(new TakePhotoEvent(mTag, paths));
                    finish();
                }
                break;
            case vin:
            case construction:
                //施工单，车架号对应服务端保存类型为PictureTypeEnum.other value值
                EventBus.getDefault().post(new TakePhotoEvent(PictureTypeEnum.other, paths));
                finish();
                break;
            default:
                EventBus.getDefault().post(new TakePhotoEvent(mTag, paths));
                finish();
                break;
        }
    }

    /**
     * 上传单张拍摄的图片
     *
     * @param file
     */
    private void uploadSinglePicture(File file) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .title("提示")
                .content("上传中")
                .cancelable(false)
                .progress(false, 100).build();
        progressDialog.show();

        HashMap<String, String> params = new HashMap<>();
        params.put("key", "attach");
        UserApi.upload(params, file, new ApiCallback<FileUploadResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                showToast("上传失败");
                progressDialog.dismiss();
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                progressDialog.setProgress((int) (progress * 100));
                LogUtils.i("wzh", progress + "");
            }

            @Override
            public void onResponse(FileUploadResp resp, int i) {
                LogUtils.i("wzh", resp.toString());
                paths.add(resp.getData().getPath());
                progressDialog.dismiss();
                handleUploadResult();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        PictureTypeEnum pictureTypeEnum = (PictureTypeEnum) getIntent().getSerializableExtra(PHOTO_TAG);
        if (pictureTypeEnum != null) {
            mTag = pictureTypeEnum;
        }
        String url = intent.getStringExtra("url");
        display(url);
    }

    private void display(String url) {
        File file = new File(url);
        if (url != null && file.exists()) {
            localPaths.add(url);
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
                if (!localPaths.isEmpty()) {
                    localPaths.remove(localPaths.size() - 1);
                }
                takeWhatPicture();
                break;
            case R.id.btn_take_one_more:
                takeWhatPicture();
                break;
            case R.id.btn_finish_take:
                if (!localPaths.isEmpty()) {
                    if (localPaths.size() == 1) {
                        uploadSinglePicture(new File(localPaths.get(0)));
                    } else {
                        //需要同时上传多张
                        uploadMultiPictures(localPaths);
                    }
                    localPaths.clear();
                }
                break;
        }
    }

    /**
     * 拍摄照片的种类
     */
    public void takeWhatPicture() {
        switch (mTag) {
            case survery:
                Intent nextTake = new Intent(mActivity, CameraActivity.class);
                nextTake.putExtra(PHOTO_TAG, mTag);
                nextTake.putExtra(PHOTO_INDEX, picIndexOfSurvery);
                startActivity(nextTake);
                break;
            default:
                Intent toTake = new Intent(mActivity, CameraActivity.class);
                toTake.putExtra(PHOTO_TAG, mTag);
                startActivity(toTake);
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}

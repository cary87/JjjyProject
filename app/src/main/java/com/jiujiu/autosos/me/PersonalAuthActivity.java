package com.jiujiu.autosos.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.resp.FileUploadResp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class PersonalAuthActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private String identiferpic1 = "";
    private String identiferPid2 = "";
    private String driverLicensePic = "";
    private String carInsurance = "";

    private int tagOfPic = -1;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("个人资料认证");
        setupToolbar(toolbar);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_personal_auth;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        saveLicensePic();
        return true;
    }

    /**
     * 保存证件照
     */
    public void saveLicensePic() {
        HashMap<String, String> params = new HashMap<>();
        params.put("identiferPic1", identiferpic1);
        params.put("identiferPic2", identiferPid2);
        params.put("driverLicensePic", driverLicensePic);
        params.put("carInsurance", carInsurance);
        showLoadingDialog("保存中");
        UserApi.saveLicense(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public void onResponse(BaseResp resp, int i) {
                hideLoadingDialog();
                showToast("保存成功");
                finish();
            }
        });
    }

    @OnClick({R.id.ll_idcard_front, R.id.ll_idcard_back, R.id.ll_driver_card, R.id.ll_insurance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_idcard_front:
                tagOfPic = 1;
                PhotoPicker.builder().setPhotoCount(1).setShowCamera(true).setPreviewEnabled(false).start(mActivity);
                break;
            case R.id.ll_idcard_back:
                tagOfPic = 2;
                PhotoPicker.builder().setPhotoCount(1).setShowCamera(true).setPreviewEnabled(false).start(mActivity);
                break;
            case R.id.ll_driver_card:
                tagOfPic = 3;
                PhotoPicker.builder().setPhotoCount(1).setShowCamera(true).setPreviewEnabled(false).start(mActivity);
                break;
            case R.id.ll_insurance:
                tagOfPic = 4;
                PhotoPicker.builder().setPhotoCount(1).setShowCamera(true).setPreviewEnabled(false).start(mActivity);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                LogUtils.i("wzh", photos.toString());
                if (photos != null && photos.size() > 0) {
                    File file = new File(photos.get(0));
                    if (file.exists()) {
                        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                                .title("提示")
                                .content("请等待")
                                .cancelable(false)
                                .progress(false, 100).build();
                        progressDialog.show();

                        HashMap<String, String> params = new HashMap<>();
                        params.put("key", "attach");
                        UserApi.upload(params, file, new ApiCallback<FileUploadResp>() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                progressDialog.dismiss();
                                showToast("上传失败");
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                                progressDialog.setProgress((int) (progress *100));
                                LogUtils.i("wzh", progress + "");
                            }

                            @Override
                            public void onResponse(FileUploadResp resp, int i) {
                                progressDialog.dismiss();
                                showToast("上传成功");
                                LogUtils.i("wzh", resp.toString());
                                switch (tagOfPic) {
                                    case 1:
                                        identiferpic1 = resp.getData().getPath();
                                        break;
                                    case 2:
                                        identiferPid2 = resp.getData().getPath();
                                        break;
                                    case 3:
                                        driverLicensePic = resp.getData().getPath();
                                        break;
                                    case 4:
                                        carInsurance = resp.getData().getPath();
                                        break;
                                }
                            }
                        });
                    }

                }
            }
        }
    }

    public static void luanchSelf(Activity activity) {
        Intent intent = new Intent(activity, PersonalAuthActivity.class);
        activity.startActivity(intent);
    }
}

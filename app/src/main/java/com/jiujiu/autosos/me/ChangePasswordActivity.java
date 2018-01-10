package com.jiujiu.autosos.me;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.base.BaseActivity;
import com.jiujiu.autosos.common.base.ChangePwdResp;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.RSACoder;
import com.jiujiu.autosos.resp.PublicKeyResp;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_old_pwd)
    TextInputEditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    TextInputEditText etNewPwd;
    @BindView(R.id.et_confirm_pwd)
    TextInputEditText etConfirmPwd;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void setup() {
        tvTitle.setText("修改密码");
        setupToolbar(toolbar);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_change_pwd;
    }

    @OnClick({R.id.btn_change_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_change_pwd:
                if (TextUtils.isEmpty(etOldPwd.getText().toString())) {
                    showToast("旧密码不能为空");
                } else if (TextUtils.isEmpty(etNewPwd.getText().toString())) {
                    showToast("新密码不能为空");
                } else if (TextUtils.isEmpty(etConfirmPwd.getText().toString())) {
                    showToast("请再次输入新密码");
                } else if (!etNewPwd.getText().toString().endsWith(etConfirmPwd.getText().toString())) {
                    showToast("两次新密码输入不一致");
                } else {
                    showLoadingDialog("密码修改中");
                    Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                            PublicKeyResp publickeyResp = UserApi.getPublicKey(PublicKeyResp.class);
                            if (isSuccessResp(publickeyResp)) {
                                String encNewPwd = RSACoder.encodeByPublicKey(etNewPwd.getText().toString(), publickeyResp.getData().getPublicKey());
                                String encOldPwd = RSACoder.encodeByPublicKey(etOldPwd.getText().toString(), publickeyResp.getData().getPublicKey());
                                HashMap<String, String> param = new HashMap<>();
                                param.put("password", encNewPwd);
                                param.put("oldPassword", encOldPwd);
                                ChangePwdResp changePwdResp = UserApi.changePwd(param, ChangePwdResp.class);
                                if (isSuccessResp(changePwdResp)) {
                                    UserStorage.getInstance().updateToken(changePwdResp.getData().getToken());
                                    emitter.onNext(changePwdResp);
                                } else {
                                    emitter.onError(new AppException(changePwdResp.getCode(), changePwdResp.getMessage()));
                                }
                            } else {
                                emitter.onError(new AppException(publickeyResp.getCode(), publickeyResp.getMessage()));
                            }

                        }
                    }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    hideLoadingDialog();
                                    showToast("密码修改成功");
                                    finish();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    handleError(throwable);
                                }
                            });
                    cd.add(disposable);
                }
                break;
        }
    }

    public static void luanchSelf(Activity activity) {
        Intent intent = new Intent(activity, ChangePasswordActivity.class);
        activity.startActivity(intent);
    }

}

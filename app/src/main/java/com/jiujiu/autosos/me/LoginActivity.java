package com.jiujiu.autosos.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.RSACoder;
import com.jiujiu.autosos.home.MainActivity;
import com.jiujiu.autosos.resp.UserResp;
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
 * Created by Administrator on 2017/12/26 0026.
 */

public class LoginActivity extends AbsBaseActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;

    @Override
    protected void setup(Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            showToast("请输入手机号");
        } else if (TextUtils.isEmpty(etPwd.getText().toString())) {
            showToast("请输入手机号");
        } else {
            showLoadingDialog("登录中");
            Disposable disposable = Observable.create(new ObservableOnSubscribe<UserResp>() {
                @Override
                public void subscribe(ObservableEmitter<UserResp> emitter) throws Exception {
                    PublicKeyResp publickeyResp = UserApi.getPublicKey(PublicKeyResp.class);
                    if (isSuccessResp(publickeyResp)) {
                        String encPwd = RSACoder.encodeByPublicKey(etPwd.getText().toString(), publickeyResp.getData().getPublicKey());
                        HashMap<String, String> param = new HashMap<>();
                        param.put("username", etPhone.getText().toString());
                        param.put("password", encPwd);
                        UserResp userResp = UserApi.login(param, UserResp.class);
                        if (isSuccessResp(userResp)) {
                            UserStorage.getInstance().setUser(userResp.getData());
                            //登录成功后设置为在线状态
                            param.clear();
                            param.put("onlineState", "0");
                            BaseResp resp = UserApi.setOnlineState(param, BaseResp.class);
                            if (isSuccessResp(resp)) {
                                UserResp.DataBean user = UserStorage.getInstance().getUser();
                                //更新在线状态后重写入持久化
                                user.setOnlineState(0);
                                UserStorage.getInstance().setUser(user);
                            }
                            emitter.onNext(userResp);
                        } else {
                            emitter.onError(new AppException(userResp.getCode(), userResp.getMessage()));
                        }
                    } else {
                        emitter.onError(new AppException(publickeyResp.getCode(), publickeyResp.getMessage()));
                    }
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<UserResp>() {
                        @Override
                        public void accept(UserResp o) throws Exception {
                            hideLoadingDialog();
                            if (UserStorage.getInstance().isServiceInfoSetted()) {
                                Intent intent = new Intent(mActivity, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(mActivity, ProvideServiceInfoActivity.class);
                                intent.putExtra("from-login", true);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            handleError(throwable);
                        }
                    });
            cd.add(disposable);
        }
    }
}

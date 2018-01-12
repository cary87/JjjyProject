package com.jiujiu.autosos.home;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.base.BaseFragment;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.nav.LocationManeger;
import com.jiujiu.autosos.resp.LoginResp;

import java.util.HashMap;
import java.util.concurrent.Callable;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class WorkbenchFragment extends BaseFragment {

    @BindView(R.id.switch_online)
    Switch switchOnline;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_work;
    }

    @Override
    protected void afterViewInited(View view) {
        switchOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                updateOnlineState(b);
            }
        });
        switchOnline.setChecked(UserStorage.getInstance().getUser().getOnlineState() == 0);
        setOnlineText(switchOnline.isChecked());
    }

    public void setOnlineText(boolean online) {
        if (online) {
            tvTitle.setText("在线");
        } else {
            tvTitle.setText("离线");
        }
    }

    /**
     * 更新在线状态
     * @param b
     */
    public void updateOnlineState(final boolean b) {
        Disposable disposable = Single.fromCallable(new Callable<BaseResp>() {
            @Override
            public BaseResp call() throws Exception {
                HashMap<String, String> param = new HashMap<>();
                param.put("onlineState", b ? "0" : "1");
                return UserApi.setOnlineState(param, BaseResp.class);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(BaseResp o) throws Exception {
                        if (mActivity.isSuccessResp(o)) {
                            LoginResp.DataBean user = UserStorage.getInstance().getUser();
                            user.setOnlineState(b ? 0 : 1);
                            UserStorage.getInstance().setUser(user);
                            setOnlineText(b);
                            if (b) {
                                LocationManeger.getInstance().startLocation();
                            } else {
                                LocationManeger.getInstance().stopLocation();
                            }
                        } else {
                            mActivity.handleError(new AppException(o.getCode(), o.getMessage()));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mActivity.handleError(throwable);
                    }
                });
        cd.add(disposable);
    }
}

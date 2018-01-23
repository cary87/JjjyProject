package com.jiujiu.autosos.common.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jiujiu.autosos.common.ActivityStack;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.utils.DialogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 所有Activity基类
 */
public abstract class AbsBaseActivity extends AppCompatActivity {

    protected CompositeDisposable cd = new CompositeDisposable();


    private Unbinder unbinder;

    private Dialog mLoadingDialog;
    protected AbsBaseActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        unbinder = ButterKnife.bind(this);
        ActivityStack.getInstance().addActivity(this);
        mActivity = this;
        onActivityCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        cd.clear();
        ActivityStack.getInstance().removeActivity(this);
    }

    protected void setupToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            setTitle(null);
        }

    }

    /**
     * 初始化数据
     */
    protected abstract void onActivityCreate(Bundle savedInstanceState);

    /**
     * 获取布局文件
     * @return
     */
    protected abstract int getLayoutID();

    public void handleError(Throwable e) {
        hideLoadingDialog();
        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            DialogUtils.showConfirmDialog(this, "服务器连接失败", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                    switch (which) {
                        case POSITIVE:
                            break;
                        case NEGATIVE:
                            break;
                    }
                }
            });
        } else if (e instanceof SocketTimeoutException) {
            mActivity.showToast("连接超时");
        } else if (e instanceof AppException) {
            AppException appException = (AppException) e;
            final String code = appException.getCode();
            if (Constant.CODE_SESSION_TIME_OUT.equals(code)) {
                DialogUtils.showConfirmDialog(this, "该账号已在其他设备登录，请重新登录", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        switch (which) {
                            case POSITIVE:
                                break;
                            case NEGATIVE:
                                break;
                        }
                    }
                });
            }else {
                mActivity.showToast(appException.getMessage());
            }
        }
    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 60);
        toast.show();
    }

    public void showLoadingDialog(String content) {
        if (mLoadingDialog == null) {
            mLoadingDialog = DialogUtils.getLoadingDialog(this,content);
        }
        mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //IntentWrapper.onBackPressed(this);
    }

    /**
     * 请求是否成功（状态码）
     * @param resp
     * @return
     */
    public boolean isSuccessResp(BaseResp resp) {
        return Constant.CODE_SUCCESS.equals(resp.getCode());
    }
}

package com.jiujiu.autosos.setting;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.code19.library.AppUtils;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseFragment;
import com.jiujiu.autosos.common.utils.AppTools;
import com.jiujiu.autosos.common.utils.DialogUtils;
import com.xdandroid.hellodaemon.IntentWrapper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class SettingFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tvBarTitle;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void afterViewInited(View view) {
        tvBarTitle.setText("设置");
    }

    @OnClick({R.id.ll_version, R.id.ll_about, R.id.ll_feedback, R.id.ll_contact_us, R.id.ll_open_protection, R.id.ll_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_version:
                DialogUtils.showConfirmDialog(mActivity, "当前为最新版本，V" + AppUtils.getAppVersionName(mActivity, mActivity.getPackageName()), null);
                break;
            case R.id.ll_about:
                AboutAcitvity.luanchSelf(mActivity);
                break;
            case R.id.ll_feedback:
                FeedbackActivity.luanchSelf(mActivity);
                break;
            case R.id.ll_contact_us:
                startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:4008820019")));
                break;
            case R.id.ll_open_protection:
                IntentWrapper.whiteListMatters(mActivity, "轨迹跟踪服务的持续运行");
                break;
            case R.id.ll_logout:
                AppTools.logout(mActivity);
                break;
        }
    }

}

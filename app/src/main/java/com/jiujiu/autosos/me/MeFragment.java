package com.jiujiu.autosos.me;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseFragment;
import com.jiujiu.autosos.common.storage.UserStorage;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class MeFragment extends BaseFragment {
    @BindView(R.id.tv_driver_name)
    TextView tvDriverName;
    @BindView(R.id.tv_driver_company)
    TextView tvDriverCompany;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_me;
    }

    @Override
    protected void afterViewInited(View view) {
        tvDriverName.setText(UserStorage.getInstance().getUser().getName());
        tvDriverCompany.setText(UserStorage.getInstance().getUser().getBelongOrgName());
    }

    @OnClick({R.id.ll_manage_service_info, R.id.ll_change_pwd, R.id.ll_my_account, R.id.ll_my_auth, R.id.ll_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_manage_service_info:
                Intent intent = new Intent(mActivity, ProvideServiceInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_change_pwd:
                ChangePasswordActivity.luanchSelf(mActivity);
                break;
            case R.id.ll_my_account:
                break;
            case R.id.ll_my_auth:
                PersonalAuthActivity.luanchSelf(mActivity);
                break;
            case R.id.ll_share:
                break;
        }
    }
}

package com.jiujiu.autosos.me;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.callback.SaveCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/31.
 */

public class AddBankCardActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_bank_name)
    EditText etBankName;
    @BindView(R.id.et_bank_account)
    EditText etBankAccount;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setupToolbar(toolbar);
        tvTitle.setText("添加银行卡");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_bankcard;
    }

    @OnClick(R.id.btn_add_bank_card)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etBankName.getText().toString())) {
            showToast("请输入银行名称");
            return;
        }
        if (TextUtils.isEmpty(etBankAccount.getText().toString())) {
            showToast("银行卡号不能为空");
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("bank", etBankName.getText().toString());
        params.put("bankAccount", etBankAccount.getText().toString());
        showLoadingDialog("正在添加");
        UserApi.addBankAccount(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public void onResponse(BaseResp baseResp, int i) {
                hideLoadingDialog();
                showToast("添加成功");
                final BankCard card = new BankCard();
                card.setBankAccount(etBankAccount.getText().toString());
                card.setBankName(etBankName.getText().toString());
                card.setUserId(UserStorage.getInstance().getUser().getUserId());
                card.saveAsync().listen(new SaveCallback() {
                    @Override
                    public void onFinish(boolean success) {
                        EventBus.getDefault().post(card);
                    }
                });
                finish();
            }
        });

    }
}

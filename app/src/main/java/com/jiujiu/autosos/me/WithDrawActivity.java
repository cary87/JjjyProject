package com.jiujiu.autosos.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 提现
 * Created by Administrator on 2018/1/31.
 */

public class WithDrawActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_account)
    TextView tvBankAccount;

    private boolean haveBankAccount = false;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setupToolbar(toolbar);
        tvTitle.setText("提现");
        DataSupport.where("userId = ?", UserStorage.getInstance().getUser().getUserId()).findAsync(BankCard.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                List<BankCard> list = (List<BankCard>) t;
                if (list != null && list.size() > 0) {
                    String bankAccount = list.get(0).getBankAccount();
                    if (!TextUtils.isEmpty(bankAccount) && bankAccount.length() > 4) {
                        String endCardNO = bankAccount.substring(bankAccount.length() - 4, bankAccount.length());
                        tvBankAccount.setText("尾号" + endCardNO + "储蓄卡");
                        tvBankName.setText(list.get(0).getBankName());
                        haveBankAccount = true;
                    }
                } else {
                    tvBankName.setText("添加一张银行卡");
                    haveBankAccount = false;
                }
            }
        });

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_withdraw;
    }

    public static void luanchSelf(Activity activity) {
        Intent intent = new Intent(activity, WithDrawActivity.class);
        activity.startActivity(intent);
    }

    @OnClick({R.id.ll_change_bank_account, R.id.btn_depost})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_change_bank_account:
                Intent intent = new Intent(this, BankCardListActivity.class);
                startActivityForResult(intent, 3333);
                break;
            case R.id.btn_depost:
                if (!haveBankAccount) {
                    showToast("先添加一张银行卡");
                    return;
                }
                if (TextUtils.isEmpty(etAmount.getText().toString())) {
                    showToast("请输入提现金额");
                    return;
                }
                withDrawAction();
                break;
        }
    }

    /**
     * 提现申请
     */
    public void withDrawAction() {
        showLoadingDialog("正在提交申请");
        HashMap<String, String> params = new HashMap<>();
        params.put("recType", RecTypeEnum.Withdraw.getValue());
        params.put("payType", "3");
        params.put("payAccount", "unknow");
        params.put("recAmount", etAmount.getText().toString());
        params.put("applyer", UserStorage.getInstance().getUser().getName());
        params.put("bank", tvBankName.getText().toString());
        params.put("bankAccount", tvBankAccount.getText().toString());
        params.put("remark", "司机提现");
        UserApi.changeAccount(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public void onResponse(BaseResp baseResp, int i) {
                hideLoadingDialog();
                showToast("提现申请提交成功");
                finish();
            }
        });
    }

}

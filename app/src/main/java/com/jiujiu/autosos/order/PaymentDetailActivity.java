package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.utils.DialogUtils;
import com.jiujiu.autosos.resp.Order;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 支付详情
 * Created by Administrator on 2018/1/16.
 */

public class PaymentDetailActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_calculation_type)
    TextView tvCalculationType;
    @BindView(R.id.tv_cross_bridge_price)
    TextView tvCrossBridgePrice;
    @BindView(R.id.tv_additional_price)
    TextView tvAdditionalPrice;
    @BindView(R.id.tv_distance)
    TextView tvDistance;

    private Order order;

    @Override
    protected void setup(Bundle savedInstanceState) {
        tvTitle.setText("救援详情");
        setupToolbar(toolbar);
        order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            tvDistance.setText(order.getDistance() + "公里");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDefaultPaymentAmount();
            }
        }, 1000);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_payment_detail;
    }

    @OnClick({R.id.fl_calculation, R.id.fl_cross_bridge, R.id.fl_additional, R.id.btn_wx_qr, R.id.btn_ali_qr})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_calculation:
                String[] calculationArray = new String[]{"公里价", "一口价"};
                DialogUtils.showSingleChoiceListDialog(mActivity, Arrays.asList(calculationArray), 0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        tvCalculationType.setText(text);
                        return true;
                    }
                }, null);
                break;
            case R.id.fl_cross_bridge:
                DialogUtils.showInputDialog(mActivity, "过桥过路费", tvCrossBridgePrice.getText().toString(), InputType.TYPE_CLASS_NUMBER, "请输入过桥过路费", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        tvCrossBridgePrice.setText(input);
                    }
                });
                break;
            case R.id.fl_additional:
                DialogUtils.showInputDialog(mActivity, "特殊加价费", tvAdditionalPrice.getText().toString(), InputType.TYPE_CLASS_NUMBER, "请输入特殊加价费", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        tvAdditionalPrice.setText(input);
                    }
                });
                break;
            case R.id.btn_wx_qr:
                break;
            case R.id.btn_ali_qr:
                break;
        }
    }

    /**
     * 拉取订单默认结算价格
     */
    public void getDefaultPaymentAmount() {
        showLoadingDialog("加载中");
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", order.getOrderId() + "");
        params.put("distance", order.getDistance() +"");
        params.put("crossBridgeAmount", "0");
        params.put("items", new Gson().toJson(order.getItemsList()));
        OrderApi.finishOrder(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public void onResponse(BaseResp resp, int i) {
                hideLoadingDialog();
            }
        });
    }
}

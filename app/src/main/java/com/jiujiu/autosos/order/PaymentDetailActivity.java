package com.jiujiu.autosos.order;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.sumimakito.awesomeqr.AwesomeQRCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.utils.DialogUtils;
import com.jiujiu.autosos.nav.LocationManeger;
import com.jiujiu.autosos.order.model.CalculationTypeEnum;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.PayWayEnum;
import com.jiujiu.autosos.order.model.RefreshViewEvent;
import com.jiujiu.autosos.resp.FinishOrderResp;
import com.jiujiu.autosos.resp.QrResp;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    @BindView(R.id.ll_other_fee)
    LinearLayout llOtherFee;

    private OrderModel order;
    private OrderItem orderItem;//取数组第一个做计费明细

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("救援详情");
        setupToolbar(toolbar);
        order = (OrderModel) getIntent().getSerializableExtra("order");
        if (order != null) {
            tvDistance.setText(order.getDistance() + "公里");
            if (order.getOrderItems() != null && order.getOrderItems().size() > 0) {
                orderItem = order.getOrderItems().get(0);
                CalculationTypeEnum calculationType = CalculationTypeEnum.getEnumByValue(orderItem.getCalculationType());
                if (calculationType != null) {
                    tvCalculationType.setText(calculationType.getLaybel());
                } else {
                    tvCalculationType.setText("");
                }
            }
        }

        getPaymentAmount();
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
                DialogUtils.showSingleChoiceListDialog(mActivity, Arrays.asList(calculationArray), -1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        tvCalculationType.setText(text);
                        CalculationTypeEnum calculationType = CalculationTypeEnum.getEnumByLabel(text.toString());
                        if (calculationType != null && calculationType.equals(CalculationTypeEnum.Once)) {
                            orderItem.setCalculationType(calculationType.getValue());
                            llOtherFee.setVisibility(View.GONE);
                            DialogUtils.showInputDialog(mActivity, "一口价费用", "", InputType.TYPE_CLASS_NUMBER, "请输入一口价", new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    double oncePrice = Double.parseDouble(input.toString());
                                    orderItem.setPrice(oncePrice);
                                    tvTotalAmount.setText(Double.toString(oncePrice) + "元");
                                    // 触发重新计算价格
                                    getPaymentAmount();
                                }
                            });
                        } else {
                            llOtherFee.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                }, null);
                break;
            case R.id.fl_cross_bridge:
                DialogUtils.showInputDialog(mActivity, "过桥过路费", tvCrossBridgePrice.getText().toString(), InputType.TYPE_CLASS_NUMBER, "请输入过桥过路费", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        tvCrossBridgePrice.setText(input);
                        order.setCrossBridgeAmount(Double.parseDouble(input.toString()));
                        getPaymentAmount();
                    }
                });
                break;
            case R.id.fl_additional:
                DialogUtils.showInputDialog(mActivity, "特殊加价费", tvAdditionalPrice.getText().toString(), InputType.TYPE_CLASS_NUMBER, "请输入特殊加价费", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        tvAdditionalPrice.setText(input);
                        orderItem.setAdditional(Double.parseDouble(input.toString()));
                        getPaymentAmount();
                    }
                });
                break;
            case R.id.btn_wx_qr:
                getQRCode(PayWayEnum.WxPay);
                break;
            case R.id.btn_ali_qr:
                getQRCode(PayWayEnum.AliPay);
                break;
        }
    }

    /**
     * 获取扫码支付二维码
     * @param payway
     */
    public void getQRCode(final PayWayEnum payway) {
        showLoadingDialog("加载中");
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", order.getOrderId() + "");
        switch (payway) {
            case AliPay:
                OrderApi.createAliQRCode(params, new ApiCallback<QrResp>() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        handleError(e);
                    }

                    @Override
                    public void onResponse(QrResp resp, int i) {
                        hideLoadingDialog();
                        generateQR2View(resp.getData().getPayQR(), payway);
                    }
                });
                break;
            case WxPay:
                OrderApi.createWechatQRCode(params, new ApiCallback<QrResp>() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        handleError(e);
                    }

                    @Override
                    public void onResponse(QrResp resp, int i) {
                        hideLoadingDialog();
                        generateQR2View(resp.getData().getPayQR(), payway);
                    }
                });
                break;
        }
    }

    /**
     * 生成二维码图片并展示
     * @param qrString
     */
    public void generateQR2View(String qrString, final PayWayEnum payWayEnum) {
        new AwesomeQRCode.Renderer()
                .contents(qrString)
                .size(500).margin(20)
                .renderAsync(new AwesomeQRCode.Callback() {
                    @Override
                    public void onRendered(AwesomeQRCode.Renderer renderer, final Bitmap bitmap) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                QRDialog qrDialog = new QRDialog(PaymentDetailActivity.this, bitmap, payWayEnum);
                                qrDialog.show();
                            }
                        });
                    }

                    @Override
                    public void onError(AwesomeQRCode.Renderer renderer, Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 拉取结算价格
     */
    public void getPaymentAmount() {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", order.getOrderId() + "");
        params.put("distance", order.getDistance() + "");
        params.put("crossBridgeAmount", Double.toString(order.getCrossBridgeAmount()));
        Gson gson = new GsonBuilder().serializeNulls().create();
        params.put("items", gson.toJson(order.getOrderItems()));
        OrderApi.finishOrder(params, new ApiCallback<FinishOrderResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public FinishOrderResp parseResponse(String string) throws Exception {
                FinishOrderResp resp = super.parseResponse(string);
                String items = resp.getData().getItems();
                List<OrderItem> orderItems = new Gson().fromJson(items, new TypeToken<List<OrderItem>>() {}.getType());
                resp.getData().setOrderItems(orderItems);
                return resp;
            }

            @Override
            public void onResponse(FinishOrderResp resp, int i) {
                tvTotalAmount.setText(resp.getData().getPayableAmount() + "元");
                EventBus.getDefault().post(resp.getData());//完成订单后发送消息给订单详情页面，以便刷新页面
                EventBus.getDefault().post(new RefreshViewEvent());//订单列表刷新
                LocationManeger.getInstance().startLocation();//接单成功时关闭位置信息更新，完成订单后重启位置信息更新
            }
        });
    }
}

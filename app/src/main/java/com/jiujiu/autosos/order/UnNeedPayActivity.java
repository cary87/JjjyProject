package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.nav.LocationManeger;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;
import com.jiujiu.autosos.order.model.RefreshViewEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/3/9.
 */

public class UnNeedPayActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private OrderModel mOrder;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("完成订单");
        mOrder = (OrderModel) getIntent().getSerializableExtra(OrderUtil.KEY_ORDER);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_unneed_pay;
    }

    @Override
    public void onBackPressed() {
    }

    @OnClick(R.id.btn_end_order)
    public void onViewClicked() {
        showLoadingDialog("订单完成中");
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", mOrder.getOrderId() + "");
        OrderApi.finishBizOrder(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public void onResponse(BaseResp baseResp, int i) {
                hideLoadingDialog();
                showToast("订单已完成");

                mOrder.setState(OrderStateEnum.Payed.getValue());
                EventBus.getDefault().post(mOrder);
                EventBus.getDefault().post(new RefreshViewEvent());
                LocationManeger.getInstance().startLocation();//接单成功时关闭位置信息更新，完成订单后重启位置信息更新

                finish();
            }
        });
    }
}

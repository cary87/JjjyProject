package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.code19.library.DateUtils;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.order.model.ChargeTypeEnum;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/18.
 */

public class OrderDetailActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_driver_owner)
    TextView tvDriverOwner;
    @BindView(R.id.tv_driver_mobile)
    TextView tvDriverMobile;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_service_type)
    TextView tvServiceType;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @BindView(R.id.tv_accept_time)
    TextView tvAcceptTime;
    @BindView(R.id.tv_charge_type)
    TextView tvChargeType;
    @BindView(R.id.tv_fee)
    TextView tvFee;

    private OrderModel mOrder;

    @Override
    protected void setup(Bundle savedInstanceState) {
        tvTitle.setText("订单详情");
        setupToolbar(toolbar);
        mOrder = (OrderModel) getIntent().getSerializableExtra("order");
        tvDriverOwner.setText(mOrder.getCarOwner());
        tvDriverMobile.setText(mOrder.getCarOwnerId() + "");
        tvOrderId.setText(mOrder.getOrderId() + "");
        tvAcceptTime.setText(DateUtils.formatDataTime(mOrder.getAcceptTime()));
        tvAddress.setText(mOrder.getAddress());
        tvChargeType.setText(ChargeTypeEnum.getType(mOrder.getChargeType()).getLabel());
        tvFee.setText("￥" + mOrder.getPayableAmount());
        tvOrderState.setText(OrderStateEnum.getOrderState(mOrder.getState()).getLabel());
        tvRemark.setText(mOrder.getRemark());
        List<OrderItem> orderItems = mOrder.getOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            tvServiceType.setText(orderItems.get(0).getItemName());
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_order_detail;
    }
}

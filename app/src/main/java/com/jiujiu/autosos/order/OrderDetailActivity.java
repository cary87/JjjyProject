package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.code19.library.DateUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.order.model.ChargeTypeEnum;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;

import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
        Disposable disposable = Single.fromCallable(new Callable<List<OrderItem>>() {
            @Override
            public List<OrderItem> call() throws Exception {
                List<OrderItem> orderItems = new Gson().fromJson(mOrder.getItems(), new TypeToken<List<OrderItem>>() {}.getType());
                return orderItems;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Function<List<OrderItem>, OrderItem>() {
                    @Override
                    public OrderItem apply(List<OrderItem> orderItems) throws Exception {
                        if (orderItems != null && orderItems.size() > 0) {
                            return orderItems.get(0);
                        }
                        return null;
                    }
                })
                .subscribe(new Consumer<OrderItem>() {
                    @Override
                    public void accept(OrderItem orderItem) throws Exception {
                        if (orderItem != null) {
                            tvServiceType.setText(orderItem.getItemName());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        cd.add(disposable);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_order_detail;
    }
}

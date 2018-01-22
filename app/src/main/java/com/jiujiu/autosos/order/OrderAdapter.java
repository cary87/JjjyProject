package com.jiujiu.autosos.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.code19.library.DateUtils;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseListAdapter;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/10.
 */

public class OrderAdapter extends BaseListAdapter<OrderModel> {

    public OrderAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        OrderViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_item_order, null);
            holder = new OrderViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (OrderViewHolder) view.getTag();
        }

        holder.tvCarNumber.setText(getItem(i).getCarNo() + "/" + getItem(i).getCarOwnerId());
        holder.tvOccurAddress.setText(getItem(i).getAddress());
        holder.tvOrderNumber.setText(getItem(i).getOrderId() +"");
        holder.tvOrderState.setText(OrderStateEnum.getOrderState(getItem(i).getState()).getLabel());
        holder.tvOrderTime.setText(DateUtils.formatDataTime(getItem(i).getAcceptTime()));
        List<OrderItem> orderItems = getItem(i).getOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            holder.tvServiceType.setText(orderItems.get(0).getItemName());
        }
        return view;
    }

    static class OrderViewHolder {
        @BindView(R.id.tv_order_number)
        TextView tvOrderNumber;
        @BindView(R.id.tv_car_number)
        TextView tvCarNumber;
        @BindView(R.id.tv_occur_address)
        TextView tvOccurAddress;
        @BindView(R.id.tv_order_state)
        TextView tvOrderState;
        @BindView(R.id.tv_order_time)
        TextView tvOrderTime;
        @BindView(R.id.tv_service_type)
        TextView tvServiceType;

        OrderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

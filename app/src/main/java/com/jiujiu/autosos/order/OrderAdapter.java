package com.jiujiu.autosos.order;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.code19.library.DateUtils;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseListAdapter;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;

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
        holder.bindData(getItem(i));
        return view;
    }



    public static class OrderViewHolder {
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
        @BindView(R.id.tv_time_name)
        TextView tvTimeName;

        public OrderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindData(OrderModel item) {
            String carNo = TextUtils.isEmpty(item.getCarNo()) ? "" : item.getCarNo() + "/";
            this.tvCarNumber.setText(carNo + item.getCarOwnerId());
            this.tvOccurAddress.setText(item.getAddress());
            this.tvOrderNumber.setText(item.getOrderId() +"");
            this.tvOrderState.setText(OrderStateEnum.getOrderState(item.getState()).getLabel());
            if (item.getAcceptTime() > 0) {
                this.tvOrderTime.setText(DateUtils.formatDataTime(item.getAcceptTime()));
            } else {
                this.tvOrderTime.setText(DateUtils.formatDataTime(item.getOrderTime()));
                this.tvTimeName.setText("下单时间：");
            }
            this.tvServiceType.setText(OrderUtil.getOrderTypeName(item));
        }
    }
}

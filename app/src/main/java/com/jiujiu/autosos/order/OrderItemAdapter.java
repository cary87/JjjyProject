package com.jiujiu.autosos.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.BaseListAdapter;
import com.jiujiu.autosos.common.utils.DialogUtils;
import com.jiujiu.autosos.order.model.CalculationTypeEnum;
import com.jiujiu.autosos.order.model.OrderItem;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/28.
 */

public class OrderItemAdapter extends BaseListAdapter<OrderItem> {

    private RecalculationHandler recalculationHandler;

    public OrderItemAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        final OrderItemViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_order_service_type, null);
            holder = new OrderItemViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (OrderItemViewHolder) view.getTag();
        }
        final OrderItem orderItem = getItem(i);
        holder.tvServiceType.setText(orderItem.getItemName());
        holder.tvAdditionalPrice.setText(orderItem.getAdditional() + "");
        CalculationTypeEnum calculationType = CalculationTypeEnum.getEnumByValue(orderItem.getCalculationType());
        if (calculationType != null) {
            holder.tvCalculationType.setText(calculationType.getLaybel());
            if (calculationType.equals(CalculationTypeEnum.Once)) {
                holder.flAdditional.setClickable(false);
            } else {
                holder.flAdditional.setClickable(true);
            }
        } else {
            holder.tvCalculationType.setText("");
        }
        holder.flCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] calculationArray = new String[]{"公里价", "一口价"};
                DialogUtils.showSingleChoiceListDialog(mContext, Arrays.asList(calculationArray), -1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        holder.tvCalculationType.setText(text);
                        CalculationTypeEnum calculationType = CalculationTypeEnum.getEnumByLabel(text.toString());
                        if (calculationType.equals(CalculationTypeEnum.Once)) {
                            holder.flAdditional.setClickable(false);
                            orderItem.setCalculationType(calculationType.getValue());
                            DialogUtils.showInputDialog(mContext, "一口价费用", "", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, "请输入一口价", new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    double oncePrice = Double.parseDouble(input.toString());
                                    orderItem.setPrice(oncePrice);
                                    if (recalculationHandler != null) {
                                        recalculationHandler.onOrderItemsChanged(mDatas);
                                    }
                                }
                            });
                        } else {
                            // 公里价触发重新计算价格
                            holder.flAdditional.setClickable(true);
                            orderItem.setCalculationType(calculationType.getValue());
                            if (recalculationHandler != null) {
                                recalculationHandler.onOrderItemsChanged(mDatas);
                            }
                        }
                        return true;
                    }
                }, null);
            }
        });
        holder.flAdditional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showInputDialog(mContext, "特殊时段加价费", holder.tvAdditionalPrice.getText().toString(), InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, "请输入特殊加价费", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        holder.tvAdditionalPrice.setText(input);
                        orderItem.setAdditional(Double.parseDouble(input.toString()));
                        if (recalculationHandler != null) {
                            recalculationHandler.onOrderItemsChanged(mDatas);
                        }
                    }
                });
            }
        });
        return view;
    }

    public interface RecalculationHandler {
        void onOrderItemsChanged(List<OrderItem> orderItems);
    }

    public void setRecalculationHandler(RecalculationHandler recalculationHandler) {
        this.recalculationHandler = recalculationHandler;
    }

    static class OrderItemViewHolder {
        @BindView(R.id.tv_service_type)
        TextView tvServiceType;
        @BindView(R.id.tv_calculation_type)
        TextView tvCalculationType;
        @BindView(R.id.fl_calculation)
        FrameLayout flCalculation;
        @BindView(R.id.tv_additional_price)
        TextView tvAdditionalPrice;
        @BindView(R.id.fl_additional)
        FrameLayout flAdditional;

        OrderItemViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

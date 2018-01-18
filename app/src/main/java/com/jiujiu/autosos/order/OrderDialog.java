package com.jiujiu.autosos.order;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.resp.Order;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/9.
 */

public class OrderDialog extends Dialog {
    @BindView(R.id.tv_order_title)
    TextView tvOrderTitle;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_ignore)
    TextView tvIgnore;
    @BindView(R.id.btn_accept_order)
    Button btnAcceptOrder;
    private Context context;
    private Order order;

    private OnAcceptOrderListener mListener;

    public OrderDialog(@NonNull Context context, Order order, OnAcceptOrderListener listener) {
        super(context);
        this.context = context;
        this.order = order;
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context, R.layout.dialog_order, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCancelable(false);

        tvPhone.setText(order.getCarOwnerId() + "");
        tvPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tvPhone.getPaint().setAntiAlias(true);//抗锯齿
        tvAddress.setText(order.getAddress());
        tvIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvPhone.getText().toString()));
                context.startActivity(intent);
            }
        });

        btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onAcceptOrder();
                    dismiss();
                }
            }
        });
    }

    public interface OnAcceptOrderListener {
        void onAcceptOrder();
    }

    public OnAcceptOrderListener getmListener() {
        return mListener;
    }

    public void setmListener(OnAcceptOrderListener mListener) {
        this.mListener = mListener;
    }
}

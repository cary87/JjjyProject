package com.jiujiu.autosos.order;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.order.model.PayWayEnum;

/**
 * Created by Administrator on 2018/1/18.
 */

public class QRDialog extends Dialog {
    private Bitmap bitmap;
    private PayWayEnum payWay;
    private ImageView ivImage;

    private Context context;

    public QRDialog(@NonNull Context context, Bitmap bitmap, PayWayEnum payWay) {
        super(context);
        this.context = context;
        this.bitmap = bitmap;
        this.payWay = payWay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (payWay.equals(PayWayEnum.WxPay)) {
            setTitle("请使用微信扫码支付");
        } else if (payWay.equals(PayWayEnum.AliPay)) {
            setTitle("请使用支付宝扫码支付");
        }
        View view = View.inflate(context, R.layout.dialog_qr, null);
        setContentView(view);
        ivImage = (ImageView) findViewById(R.id.iv_qr_image);
        ivImage.setImageBitmap(bitmap);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}

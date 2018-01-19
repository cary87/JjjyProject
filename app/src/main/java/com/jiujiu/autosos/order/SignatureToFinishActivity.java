package com.jiujiu.autosos.order;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.resp.Order;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/15.
 */

public class SignatureToFinishActivity extends AbsSignatureActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rating_comment)
    RatingBar ratingComment;
    @BindView(R.id.signature_pad)
    SignaturePad mSignaturePad;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Order order;

    @Override
    protected void setup(Bundle savedInstanceState) {
        order = (Order) getIntent().getSerializableExtra("order");
        tvTitle.setText("签名");
        setupToolbar(toolbar);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
            }
        });

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_signature;
    }

    @OnClick(R.id.btn_go_pay)
    public void onViewClicked() {
        Resources res=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.sign_tablet_bg);
        saveSignature(bmp, mSignaturePad.getSignatureBitmap(), new OnSaveCompleteListener() {
            @Override
            public void onComplete() {
                Intent intent = new Intent(SignatureToFinishActivity.this, PaymentDetailActivity.class);
                intent.putExtra("order", order);
                startActivity(intent);
                finish();
            }
        });

    }
}
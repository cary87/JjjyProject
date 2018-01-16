package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.common.base.AbsBaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/16.
 */

public class SignatureToCheckActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.signature_pad)
    SignaturePad mSignaturePad;

    @Override
    protected void setup(Bundle savedInstanceState) {
        tvTitle.setText("验车确认");
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
        return R.layout.activity_signature_check;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }


}

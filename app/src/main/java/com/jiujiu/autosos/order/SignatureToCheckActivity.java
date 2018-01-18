package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.jiujiu.autosos.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/16.
 */

public class SignatureToCheckActivity extends AbsSignatureActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.signature_pad)
    SignaturePad mSignaturePad;
    @BindView(R.id.cb_confirm)
    CheckBox cbConfirm;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (cbConfirm.isChecked()) {
            switch (item.getItemId()) {
                case R.id.menu_save:
                    saveSignature(null, mSignaturePad.getSignatureBitmap(), new OnSaveCompleteListener() {
                        @Override
                        public void onComplete() {
                            finish();
                        }
                    });
                    break;
            }
        } else {
            showToast("请勾选已验车");
        }
        return true;
    }
}

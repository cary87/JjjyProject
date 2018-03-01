package com.jiujiu.autosos.order;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.PictureTypeEnum;

import java.util.ArrayList;
import java.util.List;

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

    private OrderModel order;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        order = (OrderModel) getIntent().getSerializableExtra(OrderUtil.KEY_ORDER);
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
                        public void onComplete(String path) {
                            List<String> paths = new ArrayList<>();
                            paths.add(path);
                            OrderUtil.savePicturesForOrder(SignatureToCheckActivity.this, order, PictureTypeEnum.sign.getValue(), paths, null);
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

package com.jiujiu.autosos.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.model.ServiceItemEnum;
import com.jiujiu.autosos.common.model.YesNoEnum;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.home.MainActivity;
import com.jiujiu.autosos.req.DriverInfoReq;
import com.jiujiu.autosos.resp.LoginResp;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/4.
 */

public class ProvideServiceInfoActivity extends AbsBaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_province_of_car)
    TextView tvProvinceOfCar;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.cb_tuoche)
    CheckBox cbTuoche;
    @BindView(R.id.cb_dadian)
    CheckBox cbDadian;
    @BindView(R.id.cb_diaoche)
    CheckBox cbDiaoche;
    @BindView(R.id.cb_kuaixiu)
    CheckBox cbKuaixiu;
    @BindView(R.id.cb_kaishuo)
    CheckBox cbKaishuo;
    @BindView(R.id.cb_daijia)
    CheckBox cbDaijia;
    @BindView(R.id.cb_huanbutai)
    CheckBox cbHuanbutai;
    @BindView(R.id.cb_songshui_songyong)
    CheckBox cbSongshuiSongyong;
    @BindView(R.id.cb_kancha_paizhao)
    CheckBox cbKanchaPaizhao;
    @BindView(R.id.cb_kunjing_jiuyuan)
    CheckBox cbKunjingJiuyuan;
    @BindView(R.id.cb_daiban_nianjian)
    CheckBox cbDaibanNianjian;
    @BindView(R.id.tv_diaoche_ton)
    TextView tvDiaocheTon;
    @BindView(R.id.layout_diaoche)
    RelativeLayout layoutDiaoche;
    @BindView(R.id.tv_tuoche_ton)
    TextView tvTuocheTon;
    @BindView(R.id.rb_no1)
    RadioButton rbNo1;
    @BindView(R.id.rb_yes1)
    RadioButton rbYes1;
    @BindView(R.id.rg_luodi)
    RadioGroup rgLuodi;
    @BindView(R.id.rb_no2)
    RadioButton rbNo2;
    @BindView(R.id.rb_yes2)
    RadioButton rbYes2;
    @BindView(R.id.rg_fuzhulun)
    RadioGroup rgFuzhulun;
    @BindView(R.id.rb_no3)
    RadioButton rbNo3;
    @BindView(R.id.rb_yes3)
    RadioButton rbYes3;
    @BindView(R.id.rg_qidiao_function)
    RadioGroup rgQidiaoFunction;
    @BindView(R.id.layout_tuoche)
    LinearLayout layoutTuoche;

    @Override
    protected void setup(Bundle savedInstanceState) {
        tvTitle.setText("服务信息录入");
        setupToolbar(toolbar);
        cbTuoche.setOnCheckedChangeListener(this);
        cbDiaoche.setOnCheckedChangeListener(this);
        LoginResp.DataBean user = UserStorage.getInstance().getUser();
        if (!TextUtils.isEmpty(user.getDriverLicensePlateNumber())) {
            tvProvinceOfCar.setText(user.getDriverLicensePlateNumber().substring(0, 1));
            etCardNumber.setText(user.getDriverLicensePlateNumber().substring(1));
        }
        etMobile.setText(user.getPhone());
        if (!TextUtils.isEmpty(user.getDriverServiceItems())) {
            String itemsStr = user.getDriverServiceItems();
            List<String> arrays = Arrays.asList(itemsStr.split("|"));
            if (arrays.contains(ServiceItemEnum.DragCar.getValue() + "")) {
                cbTuoche.setChecked(true);
                layoutTuoche.setVisibility(View.VISIBLE);
                tvTuocheTon.setText(user.getDriverPlateType());
            }
            if (arrays.contains(ServiceItemEnum.TakeElec.getValue() + "")) {
                cbDadian.setChecked(true);
            }
            if (arrays.contains(ServiceItemEnum.HungCar.getValue() + "")) {
                cbDiaoche.setChecked(true);
                layoutDiaoche.setVisibility(View.VISIBLE);
                tvDiaocheTon.setText(user.getDriverToGroundType());
            }
            if (arrays.contains(ServiceItemEnum.FastReqair.getValue() + "")) {
                cbKuaixiu.setChecked(true);
            }
            if (arrays.contains(ServiceItemEnum.Unlock.getValue() + "")) {
                cbKaishuo.setChecked(true);
            }
            if (arrays.contains(ServiceItemEnum.RepalceDrive.getValue() + "")) {
                cbDaijia.setChecked(true);
            }
            if (arrays.contains(ServiceItemEnum.ChangeTire.getValue() + "")) {
                cbHuanbutai.setChecked(true);
            }
            if (arrays.contains(ServiceItemEnum.DeliverOil.getValue() + "")) {
                cbSongshuiSongyong.setChecked(true);
            }
            if (arrays.contains(ServiceItemEnum.Survery.getValue() + "")) {
                cbKanchaPaizhao.setChecked(true);
            }
            if (arrays.contains(ServiceItemEnum.PlightRescue.getValue() + "")) {
                cbKunjingJiuyuan.setChecked(true);
            }
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_service_info;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.cb_tuoche) {
            if (b) {
                layoutTuoche.setVisibility(View.VISIBLE);
            } else {
                layoutTuoche.setVisibility(View.GONE);
            }

        } else if (compoundButton.getId() == R.id.cb_diaoche) {
            if (b) {
                layoutDiaoche.setVisibility(View.VISIBLE);
            } else {
                layoutDiaoche.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            final DriverInfoReq driverInfoReq = new DriverInfoReq();
            driverInfoReq.setDriverLicensePlateNumber(tvProvinceOfCar.getText().toString() + etCardNumber.getText().toString());
            driverInfoReq.setPhone(etMobile.getText().toString());
            driverInfoReq.setDriverServiceItems("");
            StringBuffer buffer = new StringBuffer();
            if (cbTuoche.isChecked()) {
                buffer.append(ServiceItemEnum.DragCar.getValue());
                buffer.append("|");

                driverInfoReq.setDriverServiceItems(tvTuocheTon.getText().toString());

                if (rgLuodi.getCheckedRadioButtonId() == R.id.rb_yes1) {
                    driverInfoReq.setDriverToGroundType(YesNoEnum.Yes.getValue());
                } else if (rgLuodi.getCheckedRadioButtonId() == R.id.rb_no1) {
                    driverInfoReq.setDriverToGroundType(YesNoEnum.No.getValue());
                }

                if (rgFuzhulun.getCheckedRadioButtonId() == R.id.rb_yes2) {
                    driverInfoReq.setDriverAuxiliaryType(YesNoEnum.Yes.getValue());
                } else if (rgFuzhulun.getCheckedRadioButtonId() == R.id.rb_no2) {
                    driverInfoReq.setDriverAuxiliaryType(YesNoEnum.No.getValue());
                }

                if (rgQidiaoFunction.getCheckedRadioButtonId() == R.id.rb_yes3) {
                    driverInfoReq.setDriverLiftingType(YesNoEnum.Yes.getValue());
                } else if (rgQidiaoFunction.getCheckedRadioButtonId() == R.id.rb_no3) {
                    driverInfoReq.setDriverLiftingType(YesNoEnum.No.getValue());
                }
            }
            if (cbDadian.isChecked()) {
                buffer.append(ServiceItemEnum.TakeElec.getValue());
                buffer.append("|");
            }
            if (cbDiaoche.isChecked()) {
                buffer.append(ServiceItemEnum.HungCar.getValue());
                buffer.append("|");
                driverInfoReq.setDriverToGroundType(tvDiaocheTon.getText().toString());
            }
            if (cbKuaixiu.isChecked()) {
                buffer.append(ServiceItemEnum.FastReqair.getValue());
                buffer.append("|");
            }
            if (cbKaishuo.isChecked()) {
                buffer.append(ServiceItemEnum.Unlock.getValue());
                buffer.append("|");
            }
            if (cbDaijia.isChecked()) {
                buffer.append(ServiceItemEnum.RepalceDrive.getValue());
                buffer.append("|");
            }
            if (cbHuanbutai.isChecked()) {
                buffer.append(ServiceItemEnum.ChangeTire.getValue());
                buffer.append("|");
            }
            if (cbSongshuiSongyong.isChecked()) {
                buffer.append(ServiceItemEnum.DeliverOil.getValue());
                buffer.append("|");
            }
            if (cbKanchaPaizhao.isChecked()) {
                buffer.append(ServiceItemEnum.Survery.getValue());
                buffer.append("|");
            }
            if (cbKunjingJiuyuan.isChecked()) {
                buffer.append(ServiceItemEnum.PlightRescue.getValue());
                buffer.append("|");
            }
            if (cbDaibanNianjian.isChecked()) {
                buffer.append(ServiceItemEnum.YearlyCheck.getValue());
                buffer.append("|");
            }
            if (!TextUtils.isEmpty(buffer.toString()) && buffer.toString().endsWith("|")) {
                String substring = buffer.toString().substring(0, buffer.toString().length() - 1);
                driverInfoReq.setDriverServiceItems(substring);
            }

            updateServiceInfo(driverInfoReq);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateServiceInfo(final DriverInfoReq driverInfoReq) {
        showLoadingDialog("正在更新");
        Disposable disposable = Single.fromCallable(new Callable<BaseResp>() {
            @Override
            public BaseResp call() throws Exception {
                return UserApi.driverInfoUpdate(driverInfoReq, BaseResp.class);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BaseResp>() {
            @Override
            public void accept(BaseResp o) throws Exception {
                if (isSuccessResp(o)) {
                    hideLoadingDialog();
                    showToast("更新成功");
                    UserStorage.getInstance().updateServiceInfoSetted();
                    boolean isFromLogin = getIntent().getBooleanExtra("from-login", false);
                    if (isFromLogin) {
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    handleError(new AppException(o.getCode(), o.getMessage()));
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                handleError(throwable);
            }
        });
        cd.add(disposable);
    }
}

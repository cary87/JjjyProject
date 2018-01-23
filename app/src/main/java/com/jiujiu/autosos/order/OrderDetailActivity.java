package com.jiujiu.autosos.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.navi.model.NaviLatLng;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.code19.library.DateUtils;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.nav.GPSNaviActivity;
import com.jiujiu.autosos.nav.NavigateUtils;
import com.jiujiu.autosos.order.model.ChargeTypeEnum;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;
import com.jiujiu.autosos.resp.FileUploadResp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import okhttp3.Call;

import static com.jiujiu.autosos.order.model.OrderStateEnum.getTimeLineStates;

/**
 * Created by Administrator on 2018/1/18.
 */

public class OrderDetailActivity extends AbsBaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_driver_owner)
    TextView tvDriverOwner;
    @BindView(R.id.tv_driver_mobile)
    TextView tvDriverMobile;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_service_type)
    TextView tvServiceType;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_accept_time)
    TextView tvAcceptTime;
    @BindView(R.id.tv_charge_type)
    TextView tvChargeType;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.step_view)
    HorizontalStepView setpview;
    @BindView(R.id.btn_nav)
    Button btnNav;
    @BindView(R.id.btn_arrive)
    Button btnArrive;
    @BindView(R.id.btn_take_photo)
    Button btnTakePhoto;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.btn_pay)
    Button btnPay;

    private OrderModel mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onFinishOrderEvent(OrderModel orderModel) {
        this.mOrder = orderModel;
        setupView();
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("订单详情");
        setupToolbar(toolbar);
        mOrder = (OrderModel) getIntent().getSerializableExtra("order");
        setupView();
    }

    private void setupView() {
        tvDriverOwner.setText(mOrder.getCarOwner());
        tvDriverMobile.setText(mOrder.getCarOwnerId() + "");
        tvOrderId.setText(mOrder.getOrderId() + "");
        tvAcceptTime.setText(DateUtils.formatDataTime(mOrder.getAcceptTime()));
        tvAddress.setText(mOrder.getAddress());
        tvChargeType.setText(ChargeTypeEnum.getType(mOrder.getChargeType()).getLabel());
        tvFee.setText("￥" + mOrder.getPayableAmount());
        tvRemark.setText(mOrder.getRemark());
        List<OrderItem> orderItems = mOrder.getOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            tvServiceType.setText(orderItems.get(0).getItemName());
        }
        updateViewByState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void updateViewByState() {
        setSetpView();//变更timeline
        switch (OrderStateEnum.getOrderState(mOrder.getState())) {
            case Accept:
                btnNav.setVisibility(View.VISIBLE);
                btnArrive.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                break;
            case Arrive:
                btnNav.setVisibility(View.GONE);
                btnArrive.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.VISIBLE);
                btnPay.setVisibility(View.GONE);
                break;
            case Finished:
                btnNav.setVisibility(View.GONE);
                btnArrive.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                btnPay.setVisibility(View.VISIBLE);
                break;
            case Payed:
                btnNav.setVisibility(View.GONE);
                btnArrive.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_order_detail;
    }

    private void setSetpView() {
        List<StepBean> stepsBeanList = new ArrayList<>();
        for (OrderStateEnum orderStateEnum : getTimeLineStates()) {
            StepBean stepBean = new StepBean();
            stepBean.setName(orderStateEnum.getLabel());
            if (mOrder.getState() >= orderStateEnum.getValue()) {
                stepBean.setState(1);
            } else {
                stepBean.setState(-1);
            }
            stepsBeanList.add(stepBean);
        }
        setpview.setStepViewTexts(stepsBeanList)
                .setTextSize(16)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon
    }

    @OnClick({R.id.btn_nav, R.id.btn_arrive, R.id.btn_take_photo, R.id.btn_finish, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_nav:
                alertBottomSheet();
                break;
            case R.id.btn_arrive:
                sendArriveRequest();
                break;
            case R.id.btn_take_photo:
                PhotoPicker.builder().setPhotoCount(9).setShowCamera(true).setPreviewEnabled(false).start(mActivity);
                break;
            case R.id.btn_finish:
                Intent sign = new Intent(this, SignatureToFinishActivity.class);
                sign.putExtra("order", mOrder);
                startActivity(sign);
                break;
            case R.id.btn_pay:
                Intent intent = new Intent(this, PaymentDetailActivity.class);
                intent.putExtra("order", mOrder);
                startActivity(intent);
                break;
        }
    }

    /**
     * 到达救援现场请求服务端
     */
    public void sendArriveRequest() {
        showLoadingDialog("加载中");
        HashMap<String, String> params = new HashMap<>();
        params.put("province", UserStorage.getInstance().getUser().getProvince());
        params.put("orderId", mOrder.getOrderId() + "");
        OrderApi.driverArrive(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                handleError(e);
            }

            @Override
            public void onResponse(BaseResp resp, int i) {
                hideLoadingDialog();
                mOrder.setState(OrderStateEnum.Arrive.getValue());
                updateViewByState();
            }
        });
    }

    private void alertBottomSheet() {
        final BottomSheetDialog dialog = new BottomSheetDialog(mActivity);
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_two_option, null);
        dialogView.findViewById(R.id.tv_option1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NavigateUtils.isInstallByread("com.autonavi.minimap")) {
                    NaviLatLng endLatlng = new NaviLatLng(mOrder.getLatitude(), mOrder.getLongitude());
                    NavigateUtils.startAMNavi(OrderDetailActivity.this, endLatlng);
                    btnArrive.setVisibility(View.VISIBLE);
                } else {
                    showToast("请安装高德地图");
                }
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tv_option2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, GPSNaviActivity.class);
                intent.putExtra("order", mOrder);
                startActivity(intent);
                btnArrive.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                LogUtils.i("wzh", photos.toString());
                if (photos != null && photos.size() > 0) {
                    for (String photo : photos) {
                        File file = new File(photo);
                        if (file.exists()) {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("key", "attach");
                            UserApi.upload(params, file, new ApiCallback<FileUploadResp>() {
                                @Override
                                public void onError(Call call, Exception e, int i) {

                                }

                                @Override
                                public void inProgress(float progress, long total, int id) {
                                    super.inProgress(progress, total, id);
                                    LogUtils.i("wzh", progress + "");
                                }

                                @Override
                                public void onResponse(FileUploadResp resp, int i) {
                                    LogUtils.i("wzh", resp.toString());
                                    List<String> uploadPaths = new ArrayList<>();
                                    uploadPaths.add(resp.getData().getPath());
                                    OrderUtil.savePicturesForOrder(OrderDetailActivity.this, mOrder, uploadPaths);
                                }
                            });
                        }
                    }
                }
            }
        }
    }
}

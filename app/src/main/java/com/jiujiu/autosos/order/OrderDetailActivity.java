package com.jiujiu.autosos.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
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
import com.jiujiu.autosos.nav.RouteSearchManager;
import com.jiujiu.autosos.order.model.ChargeTypeEnum;
import com.jiujiu.autosos.order.model.OrderItem;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;
import com.jiujiu.autosos.order.model.RefreshViewEvent;
import com.jiujiu.autosos.resp.FileUploadResp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
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
    @BindView(R.id.btn_accept_order)
    Button btnAcceptOrder;

    private OrderModel mOrder;

    private RouteSearchManager routeSearchManager;

    private static final int MAX_TRY_COUNT = 3;

    private int currentCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onOrderStateChangeEvent(OrderModel orderModel) {
        this.mOrder = orderModel;
        setupView();
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        tvTitle.setText("订单详情");
        setupToolbar(toolbar);
        mOrder = (OrderModel) getIntent().getSerializableExtra("order");
        setupView();
        if (OrderUtil.checkIsDragcar(mOrder)) {
            getDistanceForOrder();
        }

    }

    /**
     * 获取救援距离,以便计价
     */
    private void getDistanceForOrder() {
        //在支付前计算事故地点到拖车目的地距离，如果有拖车目的地的话
        if (mOrder.getState() < OrderStateEnum.Finished.getValue() && (mOrder.getToRescueLatitude() * mOrder.getToRescueLongitude()) > 0) {
            routeSearchManager = RouteSearchManager.getInstance(this);
            routeSearchManager.setListener(new RouteSearchManager.RouteQueryListener() {
                @Override
                public void onQuerySuccess(double km, float tollDistance) {
                    mOrder.setDistance(km);
                    LogUtils.i("wzh", "救援公里 " + km + "公里" + " 高速路距离（米） " + tollDistance);
                }

                @Override
                public void onQueryFail() {
                    LogUtils.e("wzh", "获取救援距离失败");
                    currentCount++;
                    if (currentCount < MAX_TRY_COUNT) {
                        // 失败再重试
                        routeSearchManager.driverRouteQuery(new LatLonPoint(mOrder.getLatitude(), mOrder.getLongitude()), new LatLonPoint(mOrder.getToRescueLatitude(), mOrder.getToRescueLongitude()), null);
                    }

                }
            });
            routeSearchManager.driverRouteQuery(new LatLonPoint(mOrder.getLatitude(), mOrder.getLongitude()), new LatLonPoint(mOrder.getToRescueLatitude(), mOrder.getToRescueLongitude()), null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.see_photo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (TextUtils.isEmpty(mOrder.getPictures())) {
            showToast("没有图片");
        } else {
            List<String> photoPaths = Arrays.asList(mOrder.getPictures().split("\\|"));
            ArrayList<String> absolutePaths = new ArrayList<>();
            for (String photoPath : photoPaths) {
                absolutePaths.add(UserStorage.getInstance().getUser().getFastDFSFileUrlPrefix() + photoPath);
            }
            PhotoPreview.builder()
                    .setPhotos(absolutePaths)
                    .setCurrentItem(0)
                    .setShowDeleteButton(false)
                    .start(OrderDetailActivity.this);
        }
        return true;
    }

    private void setupView() {
        tvDriverOwner.setText(mOrder.getCarOwner());
        tvDriverMobile.setText(mOrder.getCarOwnerId() + "");
        tvOrderId.setText(mOrder.getOrderId() + "");
        if (mOrder.getAcceptTime() > 0) {
            tvAcceptTime.setText(DateUtils.formatDataTime(mOrder.getAcceptTime()));
        } else {
            tvAcceptTime.setText("无");
        }
        tvAddress.setText(mOrder.getAddress());
        tvChargeType.setText(ChargeTypeEnum.getType(mOrder.getChargeType()).getLabel());
        tvFee.setText("￥" + mOrder.getPayableAmount());
        tvRemark.setText(mOrder.getRemark());
        List<OrderItem> orderItems = mOrder.getOrderItems();
        if (orderItems != null && orderItems.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (OrderItem orderItem : orderItems) {
                buffer.append(orderItem.getItemName() + "-");
            }
            String itemsName = buffer.toString().substring(0, buffer.toString().length() - 1);
            tvServiceType.setText(itemsName);
        }
        updateViewByState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void updateViewByState() {
        //变更timeline
        setSetpView();
        switch (OrderStateEnum.getOrderState(mOrder.getState())) {
            case Post:
            case Order:
                btnAcceptOrder.setVisibility(View.VISIBLE);
                break;
            case Accept:
                btnNav.setVisibility(View.VISIBLE);
                btnArrive.setVisibility(View.VISIBLE);
                btnTakePhoto.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                btnAcceptOrder.setVisibility(View.GONE);
                break;
            case Arrive:
                btnNav.setVisibility(View.GONE);
                btnArrive.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.VISIBLE);
                btnPay.setVisibility(View.GONE);
                btnAcceptOrder.setVisibility(View.GONE);
                break;
            case Finished:
                btnNav.setVisibility(View.GONE);
                btnArrive.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                btnPay.setVisibility(View.VISIBLE);
                btnAcceptOrder.setVisibility(View.GONE);
                break;
            case Payed:
                btnNav.setVisibility(View.GONE);
                btnArrive.setVisibility(View.GONE);
                btnTakePhoto.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
                btnAcceptOrder.setVisibility(View.GONE);
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

    @OnClick({R.id.btn_accept_order, R.id.btn_nav, R.id.btn_arrive, R.id.btn_take_photo, R.id.btn_finish, R.id.btn_pay})
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
            case R.id.btn_accept_order:
                OrderUtil.acceptOrder(OrderDetailActivity.this, mOrder, new OrderUtil.ActionListener() {
                    @Override
                    public void onSuccess() {
                        mOrder.setState(OrderStateEnum.Accept.getValue());
                        DataSupport.deleteAll(OrderModel.class, "orderId = ?", mOrder.getOrderId() + "");
                        updateViewByState();
                        EventBus.getDefault().post(new RefreshViewEvent());
                    }

                    @Override
                    public void onFail() {

                    }
                });
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
                EventBus.getDefault().post(new RefreshViewEvent());
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

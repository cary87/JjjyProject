package com.jiujiu.autosos.nav;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.jiujiu.autosos.R;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.camera.CameraActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.DialogUtils;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.order.OrderDetailActivity;
import com.jiujiu.autosos.order.OrderUtil;
import com.jiujiu.autosos.order.SignatureToCheckActivity;
import com.jiujiu.autosos.order.SignatureToFinishActivity;
import com.jiujiu.autosos.order.model.OrderModel;
import com.jiujiu.autosos.order.model.OrderStateEnum;
import com.jiujiu.autosos.order.model.TakePhotoEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

import static com.jiujiu.autosos.order.TakePhotoConstant.PHOTO_TAG;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_ARRIVE_TAKE;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_CONSTRUCTION_TAKE;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_LOOK_TAKE;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_MOVE_UP_CAR_TAKE;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_TO_DES_TAKE;
import static com.jiujiu.autosos.order.TakePhotoConstant.TAG_VIN_TAKE;

public class GPSNaviActivity extends BaseActivity implements AMap.OnMapClickListener, View.OnClickListener {
    @BindView(R.id.btn_order_detail)
    ImageButton btnOrderDetail;
    @BindView(R.id.btn_signature)
    ImageButton btnSignature;
    @BindView(R.id.btn_look)
    ImageButton btnLook;
    @BindView(R.id.btn_vin)
    ImageButton btnVin;
    @BindView(R.id.btn_construction)
    ImageButton btnConstruction;
    private Button btnArrive;
    private TextView tvBeginNav;
    private boolean navInitSuccess;

    private AMap aMap;

    private OrderModel mOrder;

    public static final int REQ_TO_PAY = 1222;

    // 手选拖车目的地位置坐标
    private LatLng toRescueLatLng = null;
    // 中心点marker
    private Marker centerMarker;

    private RouteSearchManager routeSearchManager;

    private static final int MAX_TRY_COUNT = 3;

    private int currentCount = 0;

    private BitmapDescriptor ICON_RED = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
    private MarkerOptions markerOption = null;

    private boolean arrive = false;//是否到达事故点

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        btnArrive = (Button) findViewById(R.id.btn_arrive);
        btnArrive.setOnClickListener(this);
        tvBeginNav = (TextView) findViewById(R.id.tv_begin_nav);
        tvBeginNav.setOnClickListener(this);
        btnOrderDetail.setOnClickListener(this);
        btnSignature.setOnClickListener(this);
        btnLook.setOnClickListener(this);
        btnVin.setOnClickListener(this);
        btnConstruction.setOnClickListener(this);

        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setLayoutVisible(false);
        mAMapNaviView.setViewOptions(options);

        markerOption = new MarkerOptions().draggable(true);

        mOrder = (OrderModel) getIntent().getSerializableExtra(OrderUtil.KEY_ORDER);

        if (OrderUtil.checkIsDragcar(mOrder)) {
            getDistanceForOrder();
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_basic_navi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 监听拍照上传完成回调, 此页面不上传，OrderDetailActivity页面监听上传
     * @param event
     */
    @Subscribe
    public void onPhotoTakenEvent(final TakePhotoEvent event) {
        if (event.getPaths() != null && event.getPaths().size() > 0) {
            OrderUtil.savePicturesForOrder(this, mOrder, event.getPaths(), new OrderUtil.ActionListener() {
                @Override
                public void onSuccess() {
                    OrderUtil.addPicturePathsForOrder(mOrder, event.getPaths());
                }

                @Override
                public void onFail() {

                }
            });
        }

        if (event.getTag() == TAG_ARRIVE_TAKE) {
            btnArrive.setText("把车辆挪上拖车拍照");
            btnArrive.setTag(TAG_MOVE_UP_CAR_TAKE);
        } else if (event.getTag() == TAG_MOVE_UP_CAR_TAKE) {
            btnArrive.setText("到达拖车目的地拍照");
            btnArrive.setTag(TAG_TO_DES_TAKE);
        } else if (event.getTag() == TAG_TO_DES_TAKE) {
            Intent sign = new Intent(this, SignatureToFinishActivity.class);
            sign.putExtra(OrderUtil.KEY_ORDER, mOrder);
            startActivityForResult(sign, REQ_TO_PAY);
        }
    }

    /**
     * 显示当前自己在地图的位置
     */
    private void showMyLocationOnMap() {
        //初始化地图控制器对象
        aMap = mAMapNaviView.getMap();
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMapClickListener(this);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null) {
                    sList.clear();
                    sList.add(new NaviLatLng(location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    /**
     * 初始化起始点和终点
     */
    protected boolean setupStartAndEndLocation() {
        //已到达救援地然后导航到拖车目的地
        boolean ok = true;
        if (arrive && mOrder.getToRescueLatitude() == 0 && mOrder.getToRescueLongitude() == 0) {
            if (toRescueLatLng == null) {
                showToast("请点击地图选择一个救援目的地再进行导航");
                ok = false;
            } else {
                sList.clear();
                sList.add(new NaviLatLng(mOrder.getLatitude(), mOrder.getLongitude()));
                eList.clear();
                eList.add(new NaviLatLng(toRescueLatLng.latitude, toRescueLatLng.longitude));
            }
            return ok;
        }

        //设置救援司机起始位置，定位获取不到，取缓存
        if (sList.isEmpty()) {
            mStartLatlng = new NaviLatLng(UserStorage.getInstance().getLastSubmitLatitude(), UserStorage.getInstance().getLastSubmitLongitude());
            sList.add(mStartLatlng);
        }

        if (mOrder.getToRescueLatitude() > 0 && mOrder.getToRescueLongitude() > 0) {
            if (mOrder.getLatitude() == mOrder.getToRescueLatitude() && mOrder.getLongitude() == mOrder.getToRescueLongitude()) {
                mEndLatlng = new NaviLatLng(mOrder.getToRescueLatitude(), mOrder.getToRescueLongitude());
                eList.add(mEndLatlng);
            } else {
                mWayPointList = new ArrayList<>();
                mWayPointList.add(new NaviLatLng(mOrder.getLatitude(), mOrder.getLongitude()));
                mEndLatlng = new NaviLatLng(mOrder.getToRescueLatitude(), mOrder.getToRescueLongitude());
                eList.add(mEndLatlng);
            }
        } else {
            mEndLatlng = new NaviLatLng(mOrder.getLatitude(), mOrder.getLongitude());//救援点
            eList.add(mEndLatlng);
        }
        return ok;
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        navInitSuccess = true;
        showMyLocationOnMap();
    }

    /**
     * 开始规划导航路线
     */
    public void beginCalculateDriveRoute() {
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */

        boolean ok = setupStartAndEndLocation();//先初始化起始点和终点
        if (!ok) {
            return;
        }

        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String carNo = UserStorage.getInstance().getUser().getDriverLicensePlateNumber();
        if (!TextUtils.isEmpty(carNo) && carNo.length() > 2) {
            mAMapNavi.setCarNumber(carNo.substring(0, 1), carNo.substring(1));
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        //路线导航规划成功后，取消显示当前小蓝点
        aMap.setMyLocationEnabled(false);

        //开始导航
        mAMapNavi.startNavi(NaviType.GPS);

        //规划成功后方显示下一步操作按钮
        btnArrive.setVisibility(View.VISIBLE);

        final LBSTraceClient lbsTraceClient = LBSTraceClient.getInstance(this);
        lbsTraceClient.startTrace(new TraceStatusListener() {
            @Override
            public void onTraceStatus(List<TraceLocation> list, List<LatLng> list1, String s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_arrive:
                if (v.getTag() != null) {
                    int nowTag = (int) v.getTag();
                    if (nowTag == TAG_MOVE_UP_CAR_TAKE) {
                        Intent intent = new Intent(GPSNaviActivity.this, CameraActivity.class);
                        intent.putExtra(PHOTO_TAG, TAG_MOVE_UP_CAR_TAKE);
                        startActivity(intent);
                    } else if (nowTag == TAG_TO_DES_TAKE) {
                        DialogUtils.showConfirmDialogWithCancel(GPSNaviActivity.this, "是否到达拖车目的地", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                switch (which) {
                                    case POSITIVE:
                                        Intent intent = new Intent(GPSNaviActivity.this, CameraActivity.class);
                                        intent.putExtra(PHOTO_TAG, TAG_TO_DES_TAKE);
                                        startActivity(intent);
                                        break;
                                    case NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        });
                    }
                } else {
                    sendArriveRequest();
                }
                break;
            case R.id.tv_begin_nav:
                if (navInitSuccess) {
                    beginCalculateDriveRoute();
                }
                break;
            case R.id.btn_order_detail:
                Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                intent.putExtra(OrderUtil.KEY_ORDER, mOrder);
                startActivity(intent);
                break;
            case R.id.btn_signature:
                Intent sign = new Intent(this, SignatureToCheckActivity.class);
                sign.putExtra(OrderUtil.KEY_ORDER, mOrder);
                startActivity(sign);
                break;
            case R.id.btn_look:
                Intent look = new Intent(this, CameraActivity.class);
                look.putExtra(PHOTO_TAG, TAG_LOOK_TAKE);
                startActivity(look);
                break;
            case R.id.btn_vin:
                Intent vin = new Intent(this, CameraActivity.class);
                vin.putExtra(PHOTO_TAG, TAG_VIN_TAKE);
                startActivity(vin);
                break;
            case R.id.btn_construction:
                Intent construction = new Intent(this, CameraActivity.class);
                construction.putExtra(PHOTO_TAG, TAG_CONSTRUCTION_TAKE);
                startActivity(construction);
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
                Intent intent = new Intent(GPSNaviActivity.this, CameraActivity.class);
                intent.putExtra(PHOTO_TAG, TAG_ARRIVE_TAKE);
                startActivity(intent);
                arrive = true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_TO_PAY) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        markerOption.icon(ICON_RED);
        toRescueLatLng = latLng;
        addCenterMarker(toRescueLatLng);
        mOrder.setToRescueLatitude(latLng.latitude);
        mOrder.setToRescueLongitude(latLng.longitude);
        if (OrderUtil.checkIsDragcar(mOrder)) {
            getDistanceForOrder();
        }
    }

    private void addCenterMarker(LatLng latlng) {
        if (null == centerMarker) {
            centerMarker = aMap.addMarker(markerOption);
        }
        centerMarker.setPosition(latlng);
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
}

package com.jiujiu.autosos.nav;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.AppException;
import com.jiujiu.autosos.common.AutososApplication;
import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.model.AreaModel;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.AreaUtil;
import com.jiujiu.autosos.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/1/11.
 */

public class LocationManeger implements AMapLocationListener {
    private static LocationManeger instance;
    private AMapLocationClient mLocationClient;
    private List<OnMyLocationListener> listeners = new ArrayList<>();

    private LocationManeger() {
        //初始化定位
        mLocationClient = new AMapLocationClient(AutososApplication.getApp());
        mLocationClient.setLocationListener(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms
        option.setInterval(30000);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(option);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
        //启动定位
        mLocationClient.startLocation();
        LogUtils.i("wzh", "---------定位已启动----------");

    }

    public static LocationManeger getInstance() {
        if (instance == null) {
            synchronized (LocationManeger.class) {
                if (instance == null) {
                    instance = new LocationManeger();
                }
            }
        }
        return instance;
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        mLocationClient.stopLocation();
    }

    /**
     * 启动定位
     */
    public void startLocation() {
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                final double longitude = amapLocation.getLongitude();//获取经度
                final double latitude = amapLocation.getLatitude();//获取纬度
                amapLocation.getAccuracy();//获取精度信息
                amapLocation.getCountry();//国家信息
                String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                UserStorage.getInstance().setNowLocationAddress(address);
                String province = amapLocation.getProvince();//省信息
                String city = amapLocation.getCity();//城市信息
                LogUtils.i("wzh", "定位结果：" + province + city + " 经纬度 " + longitude + ", " + latitude + " 详细地址："  + address);
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                amapLocation.getFloor();//获取当前室内定位的楼层

                //更新司机当前位置
                if (!TextUtils.isEmpty(province) && longitude > 0 && latitude > 0) {
                    if (!listeners.isEmpty()) {
                        for (OnMyLocationListener listener : listeners) {
                            listener.onLocationChanged(longitude, latitude, province, address);
                        }
                    }
                    updateDriverPosition(longitude, latitude, province);
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtils.e("wzh", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    public void addMyLocationListener(OnMyLocationListener myLocationListener) {
        if (myLocationListener != null) {
            listeners.add(myLocationListener);
        }
    }

    public interface OnMyLocationListener {
        void onLocationChanged(final double longitude, final double latitude, final String province, final String detailAddress);
    }

    public void updateDriverPosition(final double longitude, final double latitude, final String province) {
        if (UserStorage.getInstance().isLogined() && UserStorage.getInstance().isOnline()) {
            Observable.create(new ObservableOnSubscribe<BaseResp>() {
                @Override
                public void subscribe(ObservableEmitter<BaseResp> emitter) throws Exception {
                    HashMap<String, String> param = new HashMap<>();
                    String provinceCode = "";
                    List<AreaModel> areaModels = AreaUtil.getArea(AutososApplication.getApp());
                    for (AreaModel areaModel : areaModels) {
                        if (areaModel.getName().contains(province) || (province.length() > 1 && areaModel.getName().contains(province.substring(0, 2)))) {
                            provinceCode = areaModel.getId();
                            break;
                        }
                    }
                    param.put("province", provinceCode);
                    param.put("oldlongitude", UserStorage.getInstance().getLastSubmitLongitude() == 0 ? "" : Double.toString(UserStorage.getInstance().getLastSubmitLongitude()));
                    param.put("oldlatitude", UserStorage.getInstance().getLastSubmitLatitude() == 0 ? "" : Double.toString(UserStorage.getInstance().getLastSubmitLatitude()));
                    param.put("longitude", Double.toString(longitude));
                    param.put("latitude", Double.toString(latitude));
                    BaseResp resp = UserApi.updatePosition(param, BaseResp.class);
                    if (Constant.CODE_SUCCESS.equals(resp.getCode())) {
                        UserStorage.getInstance().setLastSubmitLongitude(longitude);
                        UserStorage.getInstance().setLastSubmitLatitude(latitude);
                        emitter.onNext(resp);
                    } else {
                        emitter.onError(new AppException(resp.getCode(), resp.getMessage()));
                    }
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<BaseResp>() {
                        @Override
                        public void accept(BaseResp o) throws Exception {
                            LogUtils.i("wzh", "-------------向Server更新当前位置信息成功-----------");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("wzh", "-------------向Server更新当前位置信息失败-----------");
                        }
                    });
        }
    }

}

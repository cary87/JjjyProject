package com.jiujiu.autosos.common;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jiujiu.autosos.api.UserApi;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.common.model.AreaModel;
import com.jiujiu.autosos.common.storage.UserStorage;
import com.jiujiu.autosos.common.utils.AreaUtil;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.xdandroid.hellodaemon.AbsWorkService;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public class BackgroundWorkService extends AbsWorkService implements AMapLocationListener {
    public static boolean mShouldStopService;

    public AMapLocationClient mLocationClient = null;
    
    private static Disposable sDisposable;

    public static void stopService() {
        mShouldStopService = true;
        if (sDisposable != null) {
            sDisposable.dispose();
        }
        cancelJobAlarmSub();
    }

    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return mShouldStopService;
    }

    @Override
    public void startWork(Intent intent, int flags, int startId) {
        LogUtils.i("wzh", "startWork");
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
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
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        return null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent, Void alwaysNull) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                final Double longitude = amapLocation.getLongitude();//获取经度
                final Double latitude = amapLocation.getLatitude();//获取纬度
                amapLocation.getAccuracy();//获取精度信息
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                String province = amapLocation.getProvince();//省信息
                String city = amapLocation.getCity();//城市信息
                LogUtils.i("wzh", "定位结果：" + province + city + " 经纬度 " + longitude + ", " + latitude);
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                amapLocation.getAoiName();//获取当前定位点的AOI信息
                amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                amapLocation.getFloor();//获取当前室内定位的楼层
                
                //更新司机当前位置
                if (!TextUtils.isEmpty(province)) {
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

    public void updateDriverPosition(final Double longitude, final Double latitude, final String province) {
        if (UserStorage.getInstance().isLogined() && UserStorage.getInstance().isOnline()) {
            sDisposable = Observable.create(new ObservableOnSubscribe<BaseResp>() {
                @Override
                public void subscribe(ObservableEmitter<BaseResp> emitter) throws Exception {
                    HashMap<String, String> param = new HashMap<>();
                    String provinceCode = "";
                    List<AreaModel> areaModels = AreaUtil.getArea(getApplicationContext());
                    for (AreaModel areaModel : areaModels) {
                        if (areaModel.getName().contains(province) || (province.length() > 1 && areaModel.getName().contains(province.substring(0, 2)))) {
                            provinceCode = areaModel.getId();
                            break;
                        }
                    }
                    param.put("province", provinceCode);
                    param.put("oldlongitude", UserStorage.getInstance().getOldlongitude() == null ? "" : UserStorage.getInstance().getOldlongitude().toString());
                    param.put("oldlatitude", UserStorage.getInstance().getOldlatitude() == null ? "" : UserStorage.getInstance().getOldlatitude().toString());
                    param.put("longitude", longitude == null ? "" : longitude.toString());
                    param.put("latitude", latitude == null ? "" : latitude.toString());
                    BaseResp resp = UserApi.updatePosition(param, BaseResp.class);
                    emitter.onNext(resp);
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<BaseResp>() {
                        @Override
                        public void accept(BaseResp o) throws Exception {
                            if (Constant.CODE_SUCCESS.equals(o.getCode())) {
                                UserStorage.getInstance().setOldlongitude(longitude);
                                UserStorage.getInstance().setOldlatitude(latitude);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("wzh", "-------------向Server更新当前司机位置信息失败-----------");
                        }
                    });
            
        }
    }
}

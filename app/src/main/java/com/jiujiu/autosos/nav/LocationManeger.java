package com.jiujiu.autosos.nav;

import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jiujiu.autosos.common.AutososApplication;
import com.jiujiu.autosos.common.utils.LogUtils;
import com.jiujiu.autosos.order.OrderUtil;

/**
 * Created by Administrator on 2018/1/11.
 */

public class LocationManeger implements AMapLocationListener {
    private static LocationManeger instance;
    private AMapLocationClient mLocationClient;
    private MyLocationListener listener;

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
        option.setInterval(45000);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(option);
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
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
        //启动定位
        mLocationClient.startLocation();
        LogUtils.i("wzh", "---------连续定位已启动----------");
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
                if (!TextUtils.isEmpty(province) && longitude > 0 && latitude > 0) {
                    OrderUtil.updateDriverPosition(longitude, latitude, province, address);//更新司机位置信息
                    if (listener != null) {
                        listener.onLocationChanged(longitude, latitude, province, address);
                    }
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtils.e("wzh", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                if (listener != null) {
                    listener.onFail(amapLocation.getErrorInfo());
                }
            }
        }
    }

    public void setMyLocationListener(MyLocationListener myLocationListener) {
        listener = myLocationListener;
    }

    public interface MyLocationListener {
        void onLocationChanged(final double longitude, final double latitude, final String province, final String detailAddress);

        void onFail(String errorMsg);
    }

}

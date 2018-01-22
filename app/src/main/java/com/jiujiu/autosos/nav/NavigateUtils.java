package com.jiujiu.autosos.nav;

import android.content.Context;
import android.content.Intent;

import com.amap.api.navi.model.NaviLatLng;
import com.jiujiu.autosos.common.utils.LogUtils;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2018/1/22.
 */

public class NavigateUtils {
    private NavigateUtils() {

    }

    /**
     * 确定起终点坐标BY高德
     */
    public static void startGaodeApp(Context context, NaviLatLng start, NaviLatLng end){
        try {
            Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&slat="+start.getLatitude()+"&slon="+start.getLongitude()+"&dlat="+end.getLatitude()+"&dlon="+end.getLongitude()+"&dev=0&m=0&t=1");
            if(isInstallByread("com.autonavi.minimap")){
                context.startActivity(intent);
                LogUtils.e("wzh", "高德地图客户端已经安装") ;
            }else {
                LogUtils.e("wzh", "没有安装高德地图客户端") ;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}

package com.jiujiu.autosos.nav;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.amap.api.navi.model.NaviLatLng;

import java.io.File;

/**
 * Created by Administrator on 2018/1/22.
 */

public class NavigateUtils {
    private NavigateUtils() {

    }

    /**
     * 打开高德地图导航
     * @param context
     * @param to
     */
    public static void startAMNavi(Context context, NaviLatLng to) {
        Intent intent = new Intent();
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("androidamap://navi?sourceApplication=appname&poiname=&lat=" + to.getLatitude() + "&lon=" + to.getLongitude() + "&dev=1&style=2"));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
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

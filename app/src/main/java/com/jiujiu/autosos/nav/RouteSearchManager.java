package com.jiujiu.autosos.nav;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class RouteSearchManager implements RouteSearch.OnRouteSearchListener {

    private static RouteSearchManager instance;
    private RouteSearch mRouteSearch;

    private RouteQueryListener listener;

    private RouteSearchManager(Context context) {
        mRouteSearch = new RouteSearch(context);
        mRouteSearch.setRouteSearchListener(this);
    }

    public static RouteSearchManager getInstance(Context context) {
        if (instance == null) {
            synchronized (RouteSearchManager.class) {
                if (instance == null) {
                    instance = new RouteSearchManager(context);
                }
            }
        }
        return instance;
    }

    public void setListener(RouteQueryListener listener) {
        this.listener = listener;
    }

    public void driverRouteQuery(LatLonPoint start, LatLonPoint end, List<LatLonPoint> wayOns) {
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                start, end);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, wayOns,
                null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    DriveRouteResult mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    float tollDistance = drivePath.getTollDistance();
                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                    if (listener != null) {
                        listener.onQuerySuccess(dis/1000.00, tollDistance);
                    }

                } else if (result != null && result.getPaths() == null) {
                    if (listener != null) {
                        listener.onQueryFail();
                    }
                }

            } else {
                if (listener != null) {
                    listener.onQueryFail();
                }

            }
        } else {
            if (listener != null) {
                listener.onQueryFail();
            }
        }

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    public interface RouteQueryListener {
        void onQuerySuccess(double km, float tollDistance);

        void onQueryFail();
    }
}

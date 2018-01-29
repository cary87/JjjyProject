package com.jiujiu.autosos.api;

import com.jiujiu.autosos.common.Constant;
import com.jiujiu.autosos.common.http.ApiHelper;
import com.jiujiu.autosos.common.http.HttpMethod;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/10.
 */

public class OrderApi {
    public static final String FECTH_ORDER = "/common/order/findDriverOrders/" + Constant.PAGESIZE + "/";
    public static final String DRIVERARRIVE = "/common/order/driverArrive";
    public static final String COUNTDRIVERORDERS = "/common/order/countDriverOrders";
    public static final String ACCEPTORDER = "/common/order/driverAcceptOrder";
    public static final String FINISH_ORDER = "/common/order/finishOrder";
    public static final String SAVE_ORDER_PICTURE = "/common/order/savePicFile";
    public static final String CREATE_QR_CODE = "/common/order/createQRCode";
    public static final String FECTH_CAN_ACCEPT_ORDER = "/common/order/findDriverCanAcceptOrders/" + Constant.PAGESIZE + "/";

    public static <T> T syncFecthCanAcceptOrder(int currentPage, Class<T> clz) throws Exception {
        return ApiHelper.syncHttpRequest(HttpMethod.GET, FECTH_CAN_ACCEPT_ORDER + currentPage, null, clz);
    }

    public static <T> T syncfecthOrder(int currentPage, Class<T> clz) throws Exception {
        return ApiHelper.syncHttpRequest(HttpMethod.GET, FECTH_ORDER + currentPage, null, clz);
    }

    public static <T> void driverAcceptOrder(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, ACCEPTORDER, param, callback);
    }

    public static <T> void driverArrive(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, DRIVERARRIVE, param, callback);
    }

    public static <T> void finishOrder(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, FINISH_ORDER, param, callback);
    }

    public static <T> void countDriverOrders(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.GET, COUNTDRIVERORDERS, param, callback);
    }

    public static <T> void savePicFile(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, SAVE_ORDER_PICTURE, param, callback);
    }

    public static <T> void createQRCode(Map<String,String> param, Callback<T> callback) {
        ApiHelper.httpRequest(HttpMethod.POST, CREATE_QR_CODE, param, callback);
    }
}

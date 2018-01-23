package com.jiujiu.autosos.order;

import com.google.gson.Gson;
import com.jiujiu.autosos.api.OrderApi;
import com.jiujiu.autosos.common.base.AbsBaseActivity;
import com.jiujiu.autosos.common.http.ApiCallback;
import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.order.model.OrderModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/1/23.
 */

public class OrderUtil {

    private OrderUtil() {
        
    }

    /**
     * 保存订单相关图片
     * @param activity
     * @param order
     * @param paths
     */
    public static void savePicturesForOrder(final AbsBaseActivity activity, OrderModel order, List<String> paths) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", order.getOrderId() + "");
        params.put("pictures", new Gson().toJson(paths));
        OrderApi.savePicFile(params, new ApiCallback<BaseResp>() {
            @Override
            public void onError(Call call, Exception e, int i) {
                activity.handleError(e);
            }

            @Override
            public void onResponse(BaseResp resp, int i) {

            }
        });
    }
}

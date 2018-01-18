package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.order.model.OrderModel;

/**
 * 完成订单返回
 * Created by Administrator on 2018/1/16.
 */

public class FinishOrderResp extends BaseResp {
    private OrderModel data;

    public OrderModel getData() {
        return data;
    }

    public void setData(OrderModel data) {
        this.data = data;
    }
}

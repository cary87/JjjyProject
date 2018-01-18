package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;
import com.jiujiu.autosos.order.model.OrderModel;

import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class FecthOrderResp extends BaseResp {

    private List<OrderModel> data;

    public List<OrderModel> getData() {
        return data;
    }

    public void setData(List<OrderModel> data) {
        this.data = data;
    }

}

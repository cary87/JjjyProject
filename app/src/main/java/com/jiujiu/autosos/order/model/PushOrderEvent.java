package com.jiujiu.autosos.order.model;

/**
 * Created by Administrator on 2018/2/1.
 */

public class PushOrderEvent {
    private OrderModel order;

    public PushOrderEvent(OrderModel order) {
        this.order = order;
    }

    public OrderModel getOrder() {
        return order;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }
}

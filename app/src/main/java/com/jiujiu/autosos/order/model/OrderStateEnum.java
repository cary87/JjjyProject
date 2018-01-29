package com.jiujiu.autosos.order.model;

/**
 * 救援订单状态
 * Created by Administrator on 2018/1/10.
 */

public enum OrderStateEnum {
    Order(0, "可接单"),
    Post(1, "已派单"),
    Accept(2, "已接单"),
    Arrive(3, "已到达"),
    Finished(4, "待付款"),
    Payed(5, "已完成"),
    Evaluated(6, "已评价"),
    Cancel(7, "已取消"),
    Refused(8, "已拒绝"),
    Invoice(9, "已开票");

    private int value;
    private String label;

    OrderStateEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static OrderStateEnum getOrderState(int value) {
        for (OrderStateEnum item : values()) {
            if (item.getValue() == value) {
                return item;
            }
        }
        return null;
    }

    public static OrderStateEnum[] getTimeLineStates() {
        return new OrderStateEnum[]{
                Accept,
                Arrive,
                Finished,
                Payed};
    }
}

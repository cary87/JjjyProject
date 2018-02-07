package com.jiujiu.autosos.order.model;

/**
 * Created by Administrator on 2018/1/18.
 */

public enum PayWayEnum {
    AliPay("aliPay"),
    WxPay("wechatPay");
    private String value;

    PayWayEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

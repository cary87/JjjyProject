package com.jiujiu.autosos.order.model;

/**
 * Created by Administrator on 2018/3/1.
 */

public enum PictureTypeEnum {
    car(0, "车主下单照片"),
    arrive(1, "到达现场照片"),
    survery(2, "保险查勘"),
    moveUp(3, "挪上拖车"),
    destination(4, "到达目的地"),
    sign(5, "签名照片"),
    other(6, "其他照片"),
    vin(7, "车架号照片"),
    construction(8,"施工单照片")
    ;
    private int value;
    private String display;

    PictureTypeEnum(int value, String display) {
        this.value = value;
        this.display = display;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}

package com.jiujiu.autosos.common.model;

/**
 * Created by Administrator on 2018/1/5.
 */

public enum ServiceItemEnum {
    DragCar(0, "拖车"),
    TakeElec(1, "搭电"),
    HungCar(2, "吊车"),
    FastReqair(3, "快修"),
    Unlock(4, "开锁"),
    RepalceDrive(5, "代驾"),
    ChangeTire(6, "补胎换胎"),
    DeliverOil(7, "送水送油"),
    Survery(8, "勘查拍照"),
    PlightRescue(9, "困境救援"),
    YearlyCheck(10, "代办年检");


    ServiceItemEnum(Integer value, String lable) {
        this.value = value;
        this.lable = lable;
    }
    private Integer value;
    private  String lable;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public static ServiceItemEnum getEnum(int value) {
        for (ServiceItemEnum serviceItemEnum : values()) {
            if (serviceItemEnum.value == value) {
                return serviceItemEnum;
            }
        }

        return DragCar;
    }
}

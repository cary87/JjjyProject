package com.jiujiu.autosos.order.model;

/**
 * Created by Administrator on 2018/1/17.
 */

public enum OnlineStateEnum {
    Online(0, "在线"),
    Offline(1, "离线");

    private int value;
    private String label;

    OnlineStateEnum(int value, String label) {
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
}

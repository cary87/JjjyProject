package com.jiujiu.autosos.order.model;

/**
 * Created by Administrator on 2018/1/18.
 */

public enum ChargeTypeEnum {
    Cash(0, "收现"),
    ChageUp(1, "挂账");

    private int value;
    private String label;

    ChargeTypeEnum(int value, String label) {
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

    public static ChargeTypeEnum getType(int value) {
        for (ChargeTypeEnum chargeTypeEnum : values()) {
            if (chargeTypeEnum.value == value) {
                return chargeTypeEnum;
            }
        }
        return Cash;
    }
}

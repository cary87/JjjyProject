package com.jiujiu.autosos.order.model;

/**
 * 计价方式
 * Created by Administrator on 2018/1/17.
 */

public enum CalculationTypeEnum {
    Once(0, "一口价"),
    Keiometre(1, "公里价");

    Integer value;
    String laybel;

    CalculationTypeEnum(int value, String label) {
        this.value = value;
        this.laybel = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLaybel() {
        return laybel;
    }

    public void setLaybel(String laybel) {
        this.laybel = laybel;
    }

    public static CalculationTypeEnum getEnumByValue(Integer value) {
        for (CalculationTypeEnum calculationTypeEnum : values()) {
            if (calculationTypeEnum.value.equals(value)) {
                return calculationTypeEnum;
            }
        }

        return null;
    }

    public static CalculationTypeEnum getEnumByLabel(String label) {
        for (CalculationTypeEnum calculationTypeEnum : values()) {
            if (calculationTypeEnum.laybel.equals(label)) {
                return calculationTypeEnum;
            }
        }

        return null;
    }
}

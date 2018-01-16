package com.jiujiu.autosos.common.model;

/**
 * Created by Administrator on 2018/1/15.
 */

public enum PlateTypeEnum {
    unknow("", ""),
    one("1", "3吨/一类车"),two("2","5吨/二类车"),threee("3","8吨/三类车"),four("4", "大托/四类车"), five("5", "大托/五类车");
    private String value;
    private String lable;

    PlateTypeEnum(String value, String label) {
        this.value = value;
        this.lable = label;
    }

    public static PlateTypeEnum getPlateType(String value) {
        for (PlateTypeEnum plateTypeEnum : values()) {
            if (plateTypeEnum.value.equals(value)) {
                return plateTypeEnum;
            }
        }
        return unknow;
    }

    public static PlateTypeEnum getPlateTypeByLable(String lable) {
        for (PlateTypeEnum plateTypeEnum : values()) {
            if (plateTypeEnum.lable.equals(lable)) {
                return plateTypeEnum;
            }
        }
        return unknow;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }
}

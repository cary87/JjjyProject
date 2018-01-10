package com.jiujiu.autosos.common.model;

/**
 * Created by Administrator on 2018/1/5.
 */

public enum YesNoEnum {
    Yes("2", "有"),
    No("1", "无");
    private String value;
    private String lable;

    YesNoEnum(String value, String lable) {
        this.value = value;
        this.lable = lable;
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

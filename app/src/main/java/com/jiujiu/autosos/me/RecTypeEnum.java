package com.jiujiu.autosos.me;

/**
 * 提现充值
 * Created by Administrator on 2018/2/1.
 */

public enum RecTypeEnum {
    Recharge("1", "充值"), Withdraw("2", "提现")
    ;
    private String value;
    private String display;

    RecTypeEnum(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}

package com.jiujiu.autosos.order.model;

/**
 * Created by Administrator on 2018/1/24.
 */

public enum TraceTypeEnum {
    accept(1);

    private int type;

    private TraceTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

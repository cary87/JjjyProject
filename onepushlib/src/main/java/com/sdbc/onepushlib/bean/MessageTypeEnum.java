package com.sdbc.onepushlib.bean;

/**
 * 消息类型枚举
 * author:yangweiquan
 * create:2017/5/4
 */
public enum MessageTypeEnum implements IEnum<String> {
    /**
     * 心跳
     */
    HEARTBEAT("-1", ""),
    /**
     * 系统消息
     */
    SYSTEM("0", ""),
    /**
     * 用户消息
     */
    USER("1", "");

    private String value;
    private String display;

    MessageTypeEnum(String value, String display) {
        this.value = value;
        this.display = display;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getDisplay() {
        return this.display;
    }
}

package com.sdbc.onepushlib.bean;

/**
 * 错误码枚举
 * author:yangweiquan
 * create:2017/5/5
 */
public enum ErrorCodeEnum implements IEnum<Integer> {
    OK(1000, "成功"),
    CLIENT_SDK_NOT_INITIALIZED(1001, "推送没有初始化"),
    LOCAL_NETWORK_NOT_WORKING(1002, "没有网络"),
    SERVER_IP_PORT_NOT_SETUP(1003, "服务器ip和端口没有设置"),
    BAD_CONNECT_TO_SERVER(1004, "连接服务器失败"),
    COMMON_DATA_SEND_FAIL(1005, "数据发送失败"),
    COMMON_DATA_ENCODE_FAIL(1006, "数据编码失败");

    private int value;
    private String display;

    ErrorCodeEnum(int value, String display) {
        this.value = value;
        this.display = display;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDisplay() {
        return this.display;
    }
}

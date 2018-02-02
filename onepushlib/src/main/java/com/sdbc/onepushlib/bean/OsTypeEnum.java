package com.sdbc.onepushlib.bean;

/**
 * 系统类型
 * author:yangweiquan
 * create:2017/5/5
 */
public enum  OsTypeEnum implements IEnum<String>{

    /**
     * 华为
     */
    HWA("HWA", ""),
    /**
     * 小米
     */
    MIA("MIA", ""),
    /**
     * 其他安卓系统
     */
    OTA("OTA", ""),
    /**
     * 默认
     */
    DEFAULT(OTA.getValue(), OTA.getDisplay());

    private String value;
    private String display;

    OsTypeEnum(String value, String display) {
        this.value = value;
        this.display = display;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    public static OsTypeEnum toOsTypeEnum(String value) {
        for (OsTypeEnum osTypeEnum: values()) {
            if (osTypeEnum.getValue().equals(value)) {
                return osTypeEnum;
            }
        }

        return null;
    }
}

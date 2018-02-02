package com.sdbc.onepushlib.bean;

/**
 * App ID枚举
 * author:yangweiquan
 * create:2017/5/5
 */
public enum AppTypeEnum implements IEnum<String> {
    DOCTOR("D", 101, "", "2882303761517616243", "5581761673243"),
    PATIENT("P", 102, "", "2882303761517616226", "5101761613226"),
    AUTOSOS("A", 103, "", "2882303761517710786", "5881771070786");

    private String value;
    private int appId;
    private String display;
    private String miAppId;
    private String miAppKey;

    AppTypeEnum(String value, int appId, String display, String miAppId, String miAppKey) {
        this.value = value;
        this.appId = appId;
        this.display = display;
        this.miAppId = miAppId;
        this.miAppKey = miAppKey;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    public String getAppType() {
        return value;
    }

    public int getAppId() {
        return appId;
    }

    public String getMiAppId() {
        return miAppId;
    }

    public String getMiAppKey() {
        return miAppKey;
    }

    public static String getAppType(int appId) {
        for (AppTypeEnum appType: values()) {
            if (appId == appType.getAppId()) {
                return appType.getValue();
            }
        }

        return null;
    }
}

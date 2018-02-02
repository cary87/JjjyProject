package com.sdbc.onepushlib.bean;

import android.content.Context;

import com.sdbc.onepushlib.utils.SPCacheUtils;
import com.sdbc.onepushlib.utils.SPUtils;
import com.sdbc.onepushlib.utils.StringUtils;

import org.json.JSONObject;

/**
 * 配置类
 * author:yangweiquan
 * create:2017/5/5
 */
public class Config {

    public static final String KEY_ONE_PUSH_CLIENT_CONFIG = "one_push_client_config";

    private static Config mInstance = null;

    /**
     * 系统类型
     */
    private OsTypeEnum osType = OsTypeEnum.DEFAULT;
    /**
     * 用户ID，不能为空
     */
    private String userId;
    /**
     * token
     */
    private String token = "null";
    /**
     * app类型，不能为空
     */
    private String appType;
    /**
     * 服务端IP，不能为空
     */
    private String serverIP;
    /**
     * 服务端端口
     */
    private int serverPort = 0;
    /**
     * 上一次发送错误的情况下，下一次发送心跳包的间隔，默认为3秒
     */
    private long keepAliveIntervalError = 20000;
    /**
     * 正常情况下，下一次发送心跳包的间隔，默认为7s
     */
    private long keepAliveInterval = 30000;
    /**
     * 在使用系统推送的系统（比如小米）中，只发送几次心跳包就断开。
     */
    private int shortHeartBeatMaxCount = 5;
    /**
     * 是否为调试模式
     */
    private boolean isDebug = false;

    private Config() {
    }

    public static Config getInstance() {
        if (mInstance == null) {
            synchronized (Config.class) {
                if (mInstance == null) {
                    mInstance = new Config();
                }
            }
        }

        return mInstance;
    }

    public OsTypeEnum getOsType() {
        return osType;
    }

    public Config setOsType(OsTypeEnum osType) {
        this.osType = osType;
        return this;
    }

    /**
     * 是否为Android的不支持系统推送的操作系统（目前支持系统推送的系统有：小米）
     * @return
     */
    public boolean isOtherSystem() {
        if (this.osType == OsTypeEnum.OTA) {
            return true;
        }

        return false;
    }

    public String getUserId() {
        return userId;
    }

    public Config setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public Config setToken(String token) {
        this.token = token;
        return this;
    }

    public String getAppType() {
        return appType;
    }

    public Config setAppType(String appType) {
        this.appType = appType;
        return this;
    }

    public String getServerIP() {
        return serverIP;
    }

    public Config setServerIP(String serverIP) {
        this.serverIP = serverIP;
        return this;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Config setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public boolean hasServerIpAndPort() {
        if (StringUtils.isEmpty(this.serverIP)) {
            return false;
        }
        if (serverPort <= 0) {
            return false;
        }

        return true;
    }

    public long getKeepAliveInterval() {
        return keepAliveInterval;
    }

    public void setKeepAliveInterval(long keepAliveInterval) {
        this.keepAliveInterval = keepAliveInterval;
    }

    public long getKeepAliveIntervalError() {
        return keepAliveIntervalError;
    }

    public int getShortHeartBeatMaxCount() {
        return shortHeartBeatMaxCount;
    }

    public void setShortHeartBeatMaxCount(int shortHeartBeatMaxCount) {
        this.shortHeartBeatMaxCount = shortHeartBeatMaxCount;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public Config setDebug(boolean debug) {
        isDebug = debug;
        return this;
    }

    public boolean isConfigOk() {
        if (StringUtils.isEmpty(userId)) {
            return false;
        }
        if (StringUtils.isEmpty(appType)) {
            return false;
        }
        if (StringUtils.isEmpty(serverIP)) {
            return false;
        }
        if (serverPort <= 0) {
            return false;
        }

        return true;
    }

    /**
     * 将配置保存到本地
     */
    public void save2Local(Context context) {
        SPUtils cacheSP = SPCacheUtils.getInstance(context).getCacheSP();
        cacheSP.put(KEY_ONE_PUSH_CLIENT_CONFIG, this.toJSONString());
    }

    /**
     * 从本地加载配置
     */
    public void loadFromLocal(Context context) {
        Config config = getFromLocal(context);
        mInstance = config;
    }

    public static Config getFromLocal(Context context) {
        SPUtils cacheSP = SPCacheUtils.getInstance(context).getCacheSP();
        String json = cacheSP.getString(KEY_ONE_PUSH_CLIENT_CONFIG);
        return json2Object(json);
    }

    public void clearLocal(Context context) {
        SPUtils cacheSP = SPCacheUtils.getInstance(context).getCacheSP();
        cacheSP.remove(KEY_ONE_PUSH_CLIENT_CONFIG);
        mInstance = null;
    }

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (osType != null) {
                jsonObject.put("osType", osType.getValue());
            }
            if (StringUtils.isNotBlank(userId)) {
                jsonObject.put("userId", userId);
            }
            if (StringUtils.isNotBlank(token)) {
                jsonObject.put("token", token);
            }
            if (StringUtils.isNotBlank(appType)) {
                jsonObject.put("appType", appType);
            }
            if (StringUtils.isNotBlank(serverIP)) {
                jsonObject.put("serverIP", serverIP);
            }
            if (serverPort > 0) {
                jsonObject.put("serverPort", serverPort);
            }
            if (keepAliveInterval > 0) {
                jsonObject.put("keepAliveInterval", keepAliveInterval);
            }
            if (shortHeartBeatMaxCount > 0) {
                jsonObject.put("shortHeartBeatMaxCount", shortHeartBeatMaxCount);
            }
            jsonObject.put("isDebug", isDebug);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  jsonObject.toString();
    }

    public static Config json2Object(String json) {
        Config config = new Config();
        try {
            if (json == null || !(json.startsWith("{") && json.endsWith("}"))) {
                return config;
            }
            JSONObject jsonObject = new JSONObject(json);
            String osType = jsonObject.optString("osType");
            config.setOsType(OsTypeEnum.toOsTypeEnum(osType));
            config.setUserId(jsonObject.optString("userId"));
            config.setToken(jsonObject.optString("token"));
            config.setAppType(jsonObject.optString("appType"));
            config.setServerIP(jsonObject.optString("serverIP"));
            config.setServerPort(jsonObject.optInt("serverPort"));
            config.setKeepAliveInterval(jsonObject.optLong("keepAliveInterval"));
            config.setShortHeartBeatMaxCount(jsonObject.optInt("shortHeartBeatMaxCount"));
            config.setDebug(jsonObject.optBoolean("isDebug", false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return config;
    }
}

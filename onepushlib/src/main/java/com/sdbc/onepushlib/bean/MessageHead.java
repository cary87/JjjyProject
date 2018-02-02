package com.sdbc.onepushlib.bean;

import android.text.TextUtils;

import com.sdbc.onepushlib.utils.StringUtils;

import org.json.JSONObject;

/**
 * UDP发送消息的消息体的头部
 * author:yangweiquan
 * create:2017/5/4
 */
public class MessageHead {

    /**
     * 消息类型
     */
    private String mtype;
    /**
     * 设备类型，手机默认为M
     */
    private final String dtype = "M";
    /**
     * 设备操作系统类型
     */
    private String otype = "";
    /**
     * 用户ID
     */
    private String userid = null;
    /**
     * token
     */
    private String token = "null";
    /**
     * app类型
     */
    private String atype = null;

    public String getMtype() {
        return mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getDtype() {
        return dtype;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public String toJSONString() {
        return toJSON().toString();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtils.isNotBlank(mtype)) {
                jsonObject.put("mtype", mtype);
            }
            if (StringUtils.isNotBlank(dtype)) {
                jsonObject.put("dtype", dtype);
            }
            if (StringUtils.isNotBlank(otype)) {
                jsonObject.put("otype", otype);
            }
            if (StringUtils.isNotBlank(userid)) {
                jsonObject.put("userid", userid);
            }
            if (StringUtils.isNotBlank(token)) {
                jsonObject.put("token", token);
            }
            if (StringUtils.isNotBlank(atype)) {
                jsonObject.put("atype", atype);
            }
        } catch (Exception  e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}

package com.jiujiu.autosos.common.http;

/**
 * Created by Cary on 2017/4/19 0019.
 */
public class BaseResp {

    /**
     * code : 600000
     * message : 成功
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

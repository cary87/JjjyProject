package com.jiujiu.autosos.common;

/**
 * Created by Cary on 2017/5/3 0003.
 */
public class AppException extends Exception {
    private String code;

    public AppException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

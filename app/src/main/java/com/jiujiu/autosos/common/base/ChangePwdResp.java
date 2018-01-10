package com.jiujiu.autosos.common.base;

import com.jiujiu.autosos.common.http.BaseResp;

/**
 * Created by Administrator on 2018/1/5.
 */

public class ChangePwdResp extends BaseResp {

    /**
     * data : {"token":"f0370869db128b3a93cc31e874c9f348"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * token : f0370869db128b3a93cc31e874c9f348
         */

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}

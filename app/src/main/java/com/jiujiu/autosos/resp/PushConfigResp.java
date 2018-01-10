package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;

/**
 * 拉取推送配置返回
 */
public class PushConfigResp extends BaseResp {
    private PushConfig data;

    public PushConfig getData() {
        return data;
    }

    public void setData(PushConfig data) {
        this.data = data;
    }

    /**
     * 推送配置
     */
    public static class PushConfig {
        private String serverIP;
        private int serverPort;
        private boolean forceMIPush = false;

        public String getServerIP() {
            return serverIP;
        }

        public void setServerIP(String serverIP) {
            this.serverIP = serverIP;
        }

        public int getServerPort() {
            return serverPort;
        }

        public void setServerPort(int serverPort) {
            this.serverPort = serverPort;
        }

        public boolean isForceMIPush() {
            return forceMIPush;
        }

        public void setForceMIPush(boolean forceMIPush) {
            this.forceMIPush = forceMIPush;
        }
    }

}

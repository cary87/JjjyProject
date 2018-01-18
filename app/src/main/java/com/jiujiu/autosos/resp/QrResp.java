package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;

/**
 * Created by Administrator on 2018/1/18.
 */

public class QrResp extends BaseResp {
    private QRObj data;

    public static class QRObj {
        private String payQR;

        public String getPayQR() {
            return payQR;
        }

        public void setPayQR(String payQR) {
            this.payQR = payQR;
        }
    }

    public QRObj getData() {
        return data;
    }

    public void setData(QRObj data) {
        this.data = data;
    }
}

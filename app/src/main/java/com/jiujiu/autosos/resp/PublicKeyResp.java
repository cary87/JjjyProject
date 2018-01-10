package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;

/**
 * Created by Administrator on 2018/1/5.
 */

public class PublicKeyResp extends BaseResp {

    /**
     * data : {"publicKey":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4LD1vYrxCvZBBlSToHol/5XLt4Jyf+jwfOmfSgc8eFhnvNR9m7pel/m5viGlpDTfmYlBNhg/n+BUanuA8ctX3t3iSfw6IYLbGDZMDzOsCO/o4xLll/oWrWBVi2UM/QSwcPRCYx0kLpcJHkuzH+/CnFSz85HbBdqS21128U77PMwIDAQAB"}
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
         * publicKey : MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4LD1vYrxCvZBBlSToHol/5XLt4Jyf+jwfOmfSgc8eFhnvNR9m7pel/m5viGlpDTfmYlBNhg/n+BUanuA8ctX3t3iSfw6IYLbGDZMDzOsCO/o4xLll/oWrWBVi2UM/QSwcPRCYx0kLpcJHkuzH+/CnFSz85HbBdqS21128U77PMwIDAQAB
         */

        private String publicKey;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }
    }
}

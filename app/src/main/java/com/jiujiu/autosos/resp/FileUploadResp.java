package com.jiujiu.autosos.resp;

import com.jiujiu.autosos.common.http.BaseResp;

/**
 * Created by Cary on 2017/5/2 0002.
 */
public class FileUploadResp extends BaseResp {

    /**
     * path : /group1/M00/00/01/rBkOD1kH7riAGhV-AA6LIw-YVmE988.png
     * key : test
     * url : http://172.25.14.15:80/group1/M00/00/01/rBkOD1kH7riAGhV-AA6LIw-YVmE988.png
     */

    private DataBean data;
    private Object version;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
        this.version = version;
    }

    public static class DataBean {
        private String fileName;
        private String key;
        private String url;
        private String path;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return "OrderModel{" +
                    "fileName='" + fileName + '\'' +
                    ", key='" + key + '\'' +
                    ", url='" + url + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FileUploadResp{" +
                "data=" + data +
                ", version=" + version +
                '}';
    }
}

package com.jiujiu.autosos.order.model;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class TakePhotoEvent {
    private int tag;
    private List<String> paths;

    public TakePhotoEvent(int tag, List<String> paths) {
        this.tag = tag;
        this.paths = paths;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}

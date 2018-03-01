package com.jiujiu.autosos.order.model;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class TakePhotoEvent {
    private List<String> paths;
    private PictureTypeEnum pictureType;

    public TakePhotoEvent(PictureTypeEnum pictureType, List<String> paths) {
        this.pictureType = pictureType;
        this.paths = paths;
    }

    public PictureTypeEnum getPictureType() {
        return pictureType;
    }

    public void setPictureType(PictureTypeEnum pictureType) {
        this.pictureType = pictureType;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}

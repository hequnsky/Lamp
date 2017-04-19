package com.rmc.lamp.bean;

/**
 * 作者：hequnsky on 2016/7/28 16:22
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class RoarModel {
    public int ImageId;

    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    @Override
    public String toString() {
        return "RoarModel{" +
                "ImageId=" + ImageId +
                '}';
    }
}

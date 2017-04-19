package com.rmc.lamp.bean;

/**
 * 作者：hequnsky on 2016/7/25 14:09
 * <p>
 * 邮箱：heuqnsky@gmail.com
 */
public class InteractModel {
    public String ItemName;
    public int imageResId;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    @Override
    public String toString() {
        return "InteractModel{" +
                "ItemName='" + ItemName + '\'' +
                ", imageResId=" + imageResId +
                '}';
    }
}

package com.autohome.support.imageload.entities;

public class ImageOptions {
    private long textureId;
    private String imageUrl;//图片的加载地址
    private int width;//图片的加载宽度
    private int height;//图片的加载高度
    private int scaleType;//图片的展示缩放类型

    public String getImageUrl() {
        return imageUrl;
    }

    public ImageOptions textureId(long textureId) {
        this.textureId = textureId;
        return this;
    }

    public long getTextureId() {
        return textureId;
    }

    public ImageOptions imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public ImageOptions width(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public ImageOptions height(int height) {
        this.height = height;
        return this;
    }

    public int getScaleType() {
        return scaleType;
    }

    public ImageOptions scaleType(int scaleType) {
        this.scaleType = scaleType;
        return this;
    }
}

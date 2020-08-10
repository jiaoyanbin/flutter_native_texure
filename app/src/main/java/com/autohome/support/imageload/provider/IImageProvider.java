package com.autohome.support.imageload.provider;

import android.graphics.Bitmap;
import android.util.Log;

import com.autohome.support.imageload.entities.ImageOptions;
import com.autohome.support.imageload.interfaces.OnImageLoadListener;
import com.autohome.support.imageload.utils.LogUtil;

/**
 * bitmap 相关接口  提供bitmap和销毁bitmap
 *
 */
public interface IImageProvider {

    /**
     * 初始化bitmap供给类
     */
    void loadImage(ImageOptions imageOptions, OnImageLoadListener imageLoadListener);

    /**
     * 正在加载中展示的图
     * @return
     */
    Bitmap getLoaddingImage(ImageOptions imageOptions);

    /**
     * 加载失败展示的图
     * @return
     */
    Bitmap getFailedImage(ImageOptions imageOptions);
    /**
     * 取消加载
     */
    void cancel();

    IImageProvider EMPTY=new IImageProvider(){

        @Override
        public void loadImage(ImageOptions imageOptions, OnImageLoadListener imageLoadListener) {
            Log.e(LogUtil.TAG,"ImageTextureRender is a null IImageProvider");
        }

        @Override
        public Bitmap getLoaddingImage(ImageOptions imageOptions) {
            return null;
        }

        @Override
        public Bitmap getFailedImage(ImageOptions imageOptions) {
            return null;
        }

        @Override
        public void cancel() {
            Log.e(LogUtil.TAG,"ImageTextureRender is a null IImageProvider");
        }
    };

}

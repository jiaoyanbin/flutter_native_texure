package com.autohome.support.imageload;

import android.content.Context;

import com.autohome.support.imageload.provider.IImageProvider;

public class ImageloadManager {

    private static ImageloadManager instance;

    private OnImageProviderCallback imageProviderCallback;

    public static ImageloadManager getInstance() {
        if (instance == null) {
            synchronized (ImageloadManager.class) {
                if (instance == null) {
                    instance = new ImageloadManager();
                }
            }
        }
        return instance;
    }

    public static void init(Context application,OnImageProviderCallback imageProviderCallback){
        ImageloadManager.getInstance().imageProviderCallback=imageProviderCallback;
    }

    /**
     * 获取一个网络图片加载器
     * @return
     */
    public IImageProvider getNetImageProvider(){
        if(imageProviderCallback!=null){
            return imageProviderCallback.getNetNetImageProvider();
        }
        return null;
    }

    /**
     * 获取本地资源文件图加载器
     * @return
     */
    public IImageProvider getResourceImageProvider(){
        if(imageProviderCallback!=null){
            return imageProviderCallback.getResourceImageProvider();
        }
        return null;
    }


    public interface OnImageProviderCallback{

        /**
         * 获取一个网络图片加载器
         * @return
         */
        IImageProvider getNetNetImageProvider();

        /**
         * 获取本地资源文件图加载器
         * @return
         */
        IImageProvider getResourceImageProvider();
    }
}

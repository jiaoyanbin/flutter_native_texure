package com.autohome.support.imageload.render;

import android.content.Context;
import android.graphics.Bitmap;

import com.autohome.support.imageload.ImageloadManager;
import com.autohome.support.imageload.entities.ImageOptions;
import com.autohome.support.imageload.provider.IImageProvider;

import io.flutter.view.TextureRegistry;

/**
 * 以纹理的形式渲染bitmap图
 */
public class ResImageTextureRender extends BaseTextureRender{


    public ResImageTextureRender(Context context, TextureRegistry.SurfaceTextureEntry textureEntry, ImageOptions imageOptions) {
        super(context, textureEntry, imageOptions);
        this.imageProvider = ImageloadManager.getInstance().getResourceImageProvider();
        if(imageProvider==null){
            imageProvider= IImageProvider.EMPTY;
        }
    }

    @Override
    public void onViewCreated() {
        imageProvider.loadImage(imageOptions,this);
    }

    @Override
    public void onImageLoadSuccess(Bitmap image) {
        if(image==null){
            return;
        }
        this.openGLHelpers.createEGLEnv();
        super.createSurface(image);
        drawFrame();
        openGLHelpers.destroy();
    }

    @Override
    public void onImageLoadFail(String url) {

    }
}

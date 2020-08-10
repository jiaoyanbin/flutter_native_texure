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
public class NetImageTextureRender extends BaseTextureRender{

    public NetImageTextureRender(Context context, TextureRegistry.SurfaceTextureEntry textureEntry, ImageOptions imageOptions) {
        super(context, textureEntry, imageOptions);
        this.imageProvider = ImageloadManager.getInstance().getNetImageProvider();
        if (imageProvider == null) {
            imageProvider = IImageProvider.EMPTY;
        }
    }

    @Override
    public void onViewCreated() {
        Bitmap bitmap=imageProvider.getLoaddingImage(imageOptions);
        if(bitmap!=null) {
            this.openGLHelpers.createEGLEnv();
            createSurface(bitmap);
            drawFrame();
            openGLHelpers.destroy();
        }
        imageProvider.loadImage(imageOptions, this);
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
        Bitmap bitmap=imageProvider.getFailedImage(imageOptions);
        if(bitmap==null){
            return;
        }
        this.openGLHelpers.createEGLEnv();
        super.createSurface(bitmap);
        drawFrame();
        openGLHelpers.destroy();
    }

}

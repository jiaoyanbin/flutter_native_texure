package com.autohome.support.imageload_example.imageprovider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.autohome.support.imageload.entities.ImageOptions;
import com.autohome.support.imageload.entities.ScaleType;
import com.autohome.support.imageload.interfaces.OnImageLoadListener;
import com.autohome.support.imageload.provider.IImageProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class ResourceImageProvider implements IImageProvider {

    private final Context context;

    public ResourceImageProvider(Context context) {
        this.context = context;
    }

    public void loadImage(final ImageOptions imageOptions, final OnImageLoadListener loadListener) {
        if(TextUtils.isEmpty(imageOptions.getImageUrl())){
            return;
        }
       Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),getResource(imageOptions.getImageUrl(),context));
       if(loadListener!=null){
           loadListener.onImageLoadSuccess(bitmap);
       }
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

    }

    public int  getResource(String imageName,Context context){
        Context ctx=context.getApplicationContext();
        int resId = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }
}

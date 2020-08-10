package com.autohome.support.imageload_example.imageprovider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

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
import com.tongxue.flutter_texureview.R;

public class GlideImageProvider implements IImageProvider {

    private final Context context;
    private CustomTarget<Bitmap> target;

    public GlideImageProvider(Context context) {
        this.context = context;
    }

    public void loadImage(final ImageOptions imageOptions, final OnImageLoadListener loadListener) {
        RequestOptions options = new RequestOptions()
                .override(imageOptions.getWidth(), imageOptions.getHeight())
                .diskCacheStrategy(DiskCacheStrategy.DATA);
        switch (imageOptions.getScaleType()) {
            case ScaleType.NORMAL:

                break;
            case ScaleType.CENTER_CROP:
                options.centerCrop();
                break;
        }

        target = Glide.with(context)
                .asBitmap()
                .load(imageOptions.getImageUrl()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (loadListener != null) {
                            loadListener.onImageLoadSuccess(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (loadListener != null) {
                            loadListener.onImageLoadFail(imageOptions.getImageUrl());
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    public Bitmap getLoaddingImage(ImageOptions imageOptions) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.userpic_man_default);
    }

    @Override
    public Bitmap getFailedImage(ImageOptions imageOptions) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.userpic_man_default);
    }

    @Override
    public void cancel() {
        if (target != null) {
            Glide.with(context).clear(target);
        }
    }
}

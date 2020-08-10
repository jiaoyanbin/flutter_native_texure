package com.autohome.support.imageload_example;

import android.app.Application;

import com.autohome.support.imageload.ImageloadManager;
import com.autohome.support.imageload.provider.IImageProvider;
import com.autohome.support.imageload_example.imageprovider.GlideImageProvider;
import com.autohome.support.imageload_example.imageprovider.ResourceImageProvider;

import io.flutter.app.FlutterApplication;

public class MyApplication extends FlutterApplication implements ImageloadManager.OnImageProviderCallback {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageloadManager.init(this,this);
    }

    @Override
    public IImageProvider getNetNetImageProvider() {
        return new GlideImageProvider(this);
    }

    @Override
    public IImageProvider getResourceImageProvider() {
        return new ResourceImageProvider(this);
    }
}

package com.autohome.support.imageload;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;

import com.autohome.support.imageload.entities.ImageOptions;
import com.autohome.support.imageload.interfaces.IImageRender;
import com.autohome.support.imageload.render.NetImageTextureRender;
import com.autohome.support.imageload.render.ResImageTextureRender;
import com.autohome.support.imageload.utils.LogUtil;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.view.TextureRegistry;

/**
 * Flutter 加载图片插件 ImageloadPlugin
 * @author Created by heshiqi on 2020/6/20.
 */
public class ImageloadPlugin implements FlutterPlugin, MethodCallHandler {

    private LongSparseArray<IImageRender> mImageRenders = new LongSparseArray<>();
    private FlutterState flutterState;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        destoryAllImageRenders();
        this.flutterState =
                new FlutterState(
                        binding.getApplicationContext(),
                        binding.getBinaryMessenger(),
                        binding.getTextureRegistry());
        flutterState.startListening(this, binding.getBinaryMessenger());
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        destoryAllImageRenders();
        if (flutterState != null) {
            flutterState.stopListening(binding.getBinaryMessenger());
            flutterState = null;
        }
        LogUtil.d("hh", "onDetachedFromEngine:" + binding);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (flutterState == null) {
            return;
        }
        if (TextUtils.isEmpty(call.method)) {
            result.notImplemented();
            return;
        }
        LogUtil.d("hh", "method:" + call.method + " " + mImageRenders.size());
        int oldTextureId = call.argument("oldTextureId");
        destory(oldTextureId);
        switch (call.method) {
            case "createNetImageTexture":
                TextureRegistry.SurfaceTextureEntry entry = flutterState.textureRegistry.createSurfaceTexture();
                ImageOptions imageOptions = buildImageOptions(call,entry.id());
                NetImageTextureRender textureRender = new NetImageTextureRender(flutterState.applicationContext, entry, imageOptions);
                textureRender.onViewCreated();
                mImageRenders.put(entry.id(), textureRender);
                result.success(entry.id());
                break;
            case "createResImageTexture":
                TextureRegistry.SurfaceTextureEntry textureEntry = flutterState.textureRegistry.createSurfaceTexture();
                ImageOptions options = buildImageOptions(call,textureEntry.id());
                ResImageTextureRender resTextureRender = new ResImageTextureRender(flutterState.applicationContext, textureEntry, options);
                resTextureRender.onViewCreated();
                mImageRenders.put(textureEntry.id(), resTextureRender);
                result.success(textureEntry.id());
                break;
            case "destoryImageTexture":
                result.success(oldTextureId);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void destory(long textureId) {
        IImageRender imageRender = mImageRenders.get(textureId);
        if (imageRender != null) {
            imageRender.onViewDestroy();
        }
        mImageRenders.delete(textureId);
    }

    private void destoryAllImageRenders() {
        for (int i = 0; i < mImageRenders.size(); i++) {
            mImageRenders.valueAt(i).onViewDestroy();
        }
        mImageRenders.clear();
    }


    private ImageOptions buildImageOptions(MethodCall call,long textureId) {
        String url = call.argument("url");
        double width = call.argument("width");
        double height = call.argument("height");
        double aspectRatio = call.argument("aspectRatio");
        if (aspectRatio <= 0) {
            aspectRatio = flutterState.density;
        }
        int scaleType = call.argument("scaleType");
        ImageOptions imageOptions = new ImageOptions();
        imageOptions.textureId(textureId).imageUrl(url).width((int) (width * aspectRatio)).height((int) (height * aspectRatio)).scaleType(scaleType);
        return imageOptions;
    }

    private static final class FlutterState {
        private final Context applicationContext;
        private final BinaryMessenger binaryMessenger;
        private final TextureRegistry textureRegistry;
        private float density = 1;

        FlutterState(
                Context applicationContext,
                BinaryMessenger messenger,
                TextureRegistry textureRegistry) {
            this.applicationContext = applicationContext;
            this.binaryMessenger = messenger;
            this.textureRegistry = textureRegistry;
            this.density= Resources.getSystem().getDisplayMetrics().density;
        }

        void startListening(ImageloadPlugin methodCallHandler, BinaryMessenger messenger) {
            final MethodChannel channel = new MethodChannel(binaryMessenger, "imageload");
            channel.setMethodCallHandler(methodCallHandler);
        }

        void stopListening(BinaryMessenger messenger) {
            final MethodChannel channel = new MethodChannel(binaryMessenger, "imageload");
            channel.setMethodCallHandler(null);
        }
    }
}

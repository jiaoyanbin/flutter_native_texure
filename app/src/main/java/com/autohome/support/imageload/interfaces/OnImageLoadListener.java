package com.autohome.support.imageload.interfaces;

import android.graphics.Bitmap;

public interface OnImageLoadListener {

    void onImageLoadSuccess(Bitmap bitmap);

    void onImageLoadFail(String url);

}

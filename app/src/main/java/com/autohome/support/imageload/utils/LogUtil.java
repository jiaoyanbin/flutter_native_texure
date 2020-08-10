package com.autohome.support.imageload.utils;

import android.util.Log;

public class LogUtil {

    public static final String TAG="ImageloadPlugin";

    public static boolean isDebug=true;

    public static void d(String log){
        if(isDebug){
            d(TAG,log);
        }
    }

    public static void d(String tag,String log){
        if(isDebug){
            Log.d(tag,log);
        }
    }

    public static void w(String log){
        if(isDebug){
            Log.w(TAG,log);
        }
    }

    public static void w(String tag,String log){
        if(isDebug){
            Log.w(tag,log);
        }
    }
}

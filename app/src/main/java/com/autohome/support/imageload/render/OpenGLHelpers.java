package com.autohome.support.imageload.render;

import android.graphics.SurfaceTexture;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import io.flutter.view.TextureRegistry;

public class OpenGLHelpers {

    private TextureRegistry.SurfaceTextureEntry textureEntry;
    private  SurfaceTexture texture;
    private EGL10 egl;
    private EGLDisplay eglDisplay;
    private EGLContext eglContext;
    private EGLSurface eglSurface;

    public OpenGLHelpers(TextureRegistry.SurfaceTextureEntry textureEntry,SurfaceTexture texture) {
        this.textureEntry = textureEntry;
        this.texture=texture;
    }

    public void createEGLEnv(){
        //初始化EGL
        egl = (EGL10) EGLContext.getEGL();
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (eglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }
        int[] version = new int[2];
        if (!egl.eglInitialize(eglDisplay, version)) {
            throw new RuntimeException("eglInitialize failed");
        }

        //确定可用表面配置
        EGLConfig eglConfig = chooseEglConfig();

        //创建渲染窗口 当找到符合渲染需求的EGLConfig后，就可以创建显然窗口， 调用函数如下:
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null);

        //创建一个渲染上下文 其中share_context表示是否有context共享，共享的contxt之间亦共享所有数据，EGL_NO_CONTEXT代表不共享；attrib_list表示可用属性，当前只有EGL_CONTEXT_CLIENT_VERSION, 1代表OpenGL ES 1.x, 2代表2.0。
        eglContext = createContext(egl, eglDisplay, eglConfig);

        if (eglSurface == null || eglSurface == EGL10.EGL_NO_SURFACE) {
            throw new RuntimeException("GL Error: " + GLUtils.getEGLErrorString(egl.eglGetError()));
        }

        if (!egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            throw new RuntimeException("GL make current error: " + GLUtils.getEGLErrorString(egl.eglGetError()));
        }
    }

    //让EGL选择配置
    private EGLConfig chooseEglConfig() {
        int chooseNum = 1;
        EGLConfig chooseConfigs[] = new EGLConfig[chooseNum];
        int chooseMaxNum[] = new int[1];

        int[] attributes = new int[]{
                EGL10.EGL_RENDERABLE_TYPE, 4,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_STENCIL_SIZE, 0,
                EGL10.EGL_SAMPLE_BUFFERS, 1,
                EGL10.EGL_SAMPLES, 4,
                EGL10.EGL_NONE
        };

        if (!egl.eglChooseConfig(eglDisplay, attributes, chooseConfigs, 1, chooseMaxNum)) {
            throw new IllegalArgumentException("Failed to choose config: " + GLUtils.getEGLErrorString(egl.eglGetError()));
        } else if (chooseMaxNum[0] > 0) {
            return chooseConfigs[0];
        }

        return null;
    }

    private EGLContext createContext(EGL10 egl, EGLDisplay eglDisplay, EGLConfig eglConfig) {
        int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
        int[] attribList = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
        return egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attribList);
    }

    public void destroy(){
        egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(eglDisplay, eglSurface);
        egl.eglDestroyContext(eglDisplay, eglContext);
        egl.eglTerminate(eglDisplay);
    }

    public void releaseSurface(){
        if (textureEntry != null) {
            textureEntry.release();
        }
        if (texture != null) {
            texture.release();
        }
    }

    public  void swapBuffer(){
        egl.eglSwapBuffers(eglDisplay, eglSurface);
    }

}

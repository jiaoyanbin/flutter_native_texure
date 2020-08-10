package com.autohome.support.imageload.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.autohome.support.imageload.entities.ImageOptions;
import com.autohome.support.imageload.interfaces.IImageRender;
import com.autohome.support.imageload.interfaces.OnImageLoadListener;
import com.autohome.support.imageload.provider.IImageProvider;
import com.autohome.support.imageload.utils.LogUtil;
import com.autohome.support.imageload.utils.ProjectionMatrixHelper;
import com.autohome.support.imageload.utils.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import io.flutter.view.TextureRegistry;

/**
 * 以纹理的形式渲染bitmap图
 */
public abstract class BaseTextureRender implements IImageRender, OnImageLoadListener {

    private static final String TAG = "BaseTextureRender";

    protected Context context;
    protected IImageProvider imageProvider;
    protected ImageOptions imageOptions;//图片参数
    protected OpenGLHelpers openGLHelpers;

    private String VERTEX_SHADER =
            "uniform mat4 u_Matrix;\n" +
                    "attribute vec4 a_Position;\n" +
                    "// 纹理坐标：2个分量，S和T坐标\n" +
                    "attribute vec2 a_TexCoord;\n" +
                    "varying vec2 v_TexCoord;\n" +
                    "void main() {\n" +
                    "     v_TexCoord = a_TexCoord;\n" +
                    "     gl_Position = u_Matrix * a_Position;\n" +
                    "}";
    private String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 v_TexCoord;\n" +
                    "// sampler2D：二维纹理数据的数组\n" +
                    "uniform sampler2D u_TextureUnit;\n" +
                    "void main() {\n" +
                    "     gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);\n" +
                    "}";

    /**
     * 顶点坐标中每个点占的向量个数
     */
    private static final int POSITION_COMPONENT_COUNT = 2;

    /**
     * 顶点
     */
    private static float[] POINT_DATA = {
            -1f, -1f,
            -1f, 1f,
            1f, 1f,
            1f, -1f
    };

    /**
     * 纹理坐标
     */
    private static float[] TEX_VERTEX = {
            0f, 1f,
            0f, 0f,
            1f, 0f,
            1f, 1f
    };
    /**
     * 纹理坐标中每个点占的向量个数
     */
    private int TEX_VERTEX_COMPONENT_COUNT = 2;

    //位置
    private static FloatBuffer mVertexData;
    //纹理
    private static FloatBuffer mTexVertexBuffer;

    private int uTextureUnitLocation;
    private ProjectionMatrixHelper mProjectionMatrixHelper;

    private int program;

    static {
        mVertexData = ByteBuffer.allocateDirect(POINT_DATA.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(POINT_DATA);
        mTexVertexBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_VERTEX);
    }


    public BaseTextureRender(Context context, TextureRegistry.SurfaceTextureEntry textureEntry,ImageOptions imageOptions) {
        this.context = context;
        this.imageOptions = imageOptions;
        SurfaceTexture surfaceTexture = textureEntry.surfaceTexture();
        surfaceTexture.setDefaultBufferSize(imageOptions.getWidth(), imageOptions.getHeight());
        this.openGLHelpers = new OpenGLHelpers(textureEntry, surfaceTexture);
    }

    /**
     * 创建GLES20 初始化参数
     *
     * @param bitmap
     */
    protected void createSurface(Bitmap bitmap) {
        makeProgram(VERTEX_SHADER, FRAGMENT_SHADER);
        int aPositionLocation = GLES20.glGetAttribLocation(program, "a_Position");
        mProjectionMatrixHelper = new ProjectionMatrixHelper(program, "u_Matrix");

        // 纹理坐标索引
        int aTexCoordLocation = GLES20.glGetAttribLocation(program, "a_TexCoord");
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, "u_TextureUnit");

        loadTexture((int) imageOptions.getTextureId(), bitmap);

        mVertexData.position(0);
        //设置顶点位置值
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mVertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        // 加载纹理坐标
        mTexVertexBuffer.position(0);
        GLES20.glVertexAttribPointer(aTexCoordLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, mTexVertexBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoordLocation);

        GLES20.glClearColor(0f, 0f, 0f, 1f);
        // 开启纹理透明混合，这样才能绘制透明图片
        GLES20.glEnable(GL10.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glViewport(0, 0, imageOptions.getWidth(), imageOptions.getHeight());
        mProjectionMatrixHelper.enable(imageOptions.getWidth(), imageOptions.getHeight());
    }


    /**
     * 创建OpenGL程序对象
     *
     * @param vertexShader   顶点着色器代码
     * @param fragmentShader 片段着色器代码
     */
    protected void makeProgram(String vertexShader, String fragmentShader) {
        // 步骤1：编译顶点着色器
        int vertexShaderId = ShaderUtil.compileVertexShader(vertexShader);
        // 步骤2：编译片段着色器
        int fragmentShaderId = ShaderUtil.compileFragmentShader(fragmentShader);
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        program = ShaderUtil.linkProgram(vertexShaderId, fragmentShaderId);

        if (LogUtil.isDebug) {
            ShaderUtil.validateProgram(program);
        }

        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(program);
    }

    protected void loadTexture(int textureId, Bitmap bitmap) {
        // 2. 将纹理绑定到OpenGL对象上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        // 3. 设置纹理过滤参数:解决纹理缩放过程中的锯齿问题。若不设置，则会导致纹理为黑色
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        if (bitmap != null) {
            // 4. 通过OpenGL对象读取Bitmap数据，并且绑定到纹理对象上，之后就可以回收Bitmap对象
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            // 5. 生成Mip位图
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
//            bitmap.recycle();
            // 7. 将纹理从OpenGL对象上解绑
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            // 所以整个流程中，OpenGL对象类似一个容器或者中间者的方式，将Bitmap数据转移到OpenGL纹理上
        }
    }


    @Override
    public void onViewDestroy() {
//                GLES20.glDeleteTextures(1, new int[]{(int)imageOptions.getTextureId()}, 0);
        imageProvider.cancel();
        openGLHelpers.releaseSurface();

    }


    protected void drawFrame() {
        GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);
        // 纹理单元：在OpenGL中，纹理不是直接绘制到片段着色器上，而是通过纹理单元去保存纹理

        // 设置当前活动的纹理单元为纹理单元0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // 将纹理ID绑定到当前活动的纹理单元上
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, (int) imageOptions.getTextureId());

//        // 将纹理单元传递片段着色器的u_TextureUnit
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, POINT_DATA.length / POSITION_COMPONENT_COUNT);

        this.openGLHelpers.swapBuffer();
    }
}

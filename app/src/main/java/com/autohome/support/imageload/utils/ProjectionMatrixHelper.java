package com.autohome.support.imageload.utils;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class ProjectionMatrixHelper {

    private int uMatrixLocation;
    private float[] mProjectionMatrix;

    public ProjectionMatrixHelper(int program, String name) {
        uMatrixLocation = GLES20.glGetUniformLocation(program, name);

        mProjectionMatrix = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    }

    public void enable(int width, int height) {
        float aspectRatio;

        if (width > height) {
            aspectRatio = (float) width / (float) height;
        } else {
            aspectRatio = (float) height / (float) width;
        }

        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
    }

}

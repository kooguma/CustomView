package com.kumaj.customview.test;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class TranslateAnimation extends Animation {

    private float mTranslateX;
    private float mTranslateY;
    private float mTranslateZ;

    private Camera mCamera;

    public TranslateAnimation(float x, float y, float z) {
        mTranslateX = x;
        mTranslateY = y;
        mTranslateZ = z;
    }

    @Override public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        final Camera camera = mCamera;
        final Matrix matrix = t.getMatrix();
        float x = interpolatedTime * mTranslateX;
        float y = interpolatedTime * mTranslateY;
        float z = interpolatedTime * mTranslateZ;
        //set camera
        camera.translate(x,y,z);
        //set matrix
        camera.getMatrix(matrix);

    }
}

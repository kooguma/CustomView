package com.kumaj.loadingdrawable.musicbar;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;

public class MusicBarDrawable extends Drawable {

    private static final String TAG = "MusicBarDrawable";

    private int mWidth;
    private int mHeight;

    private final Rect mBounds = new Rect();

    private Params mParams;

    private ValueAnimator mAnimator;
    private float mRatio;

    private static final float[] sRatios = new float[] { 0.7f, 1.0f, 0.75f, 0.8f };

    private MusicBarDrawable(Params params) {
        mParams = params;
        setupAnimator();
    }

    private void setupAnimator() {
        mAnimator = ValueAnimator.ofFloat(1f, 0.5f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mRatio = (float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });

        mAnimator.setDuration(350);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);

        mAnimator.start();
    }

    @Override protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.left = bounds.left;
        mBounds.right = bounds.right;
        mBounds.top = bounds.top;
        mBounds.bottom = bounds.bottom;
    }

    @Override public void draw(@NonNull Canvas canvas) {
        final int left = mBounds.left;
        final int width = mBounds.width();
        final int rectWidth = (int) (width /
            ((mParams.mRectCount + 1) * mParams.mRatio + mParams.mRectCount));
        final int gapWidth = (int) (rectWidth * mParams.mRatio);

        final RectF rect = new RectF();
        rect.top = mBounds.top;
        rect.bottom = mBounds.bottom;
        float ratio = 0;

        for (int i = 0; i < mParams.mRectCount; i++) {
            rect.left = left + i * (gapWidth + rectWidth);
            rect.right = rect.left + rectWidth;
            if (i % 2 == 0) {
                ratio = (1.5f - mRatio);
                rect.top = mBounds.bottom * (1.0f - ratio * sRatios[i]);
            } else {
                ratio = mRatio;
                rect.top = mBounds.bottom * (1.0f - ratio * sRatios[i]);
            }
            canvas.drawRect(rect, mParams.mPaint);
        }

    }

    @Override public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        if (mParams.mPaint != null) {
            mParams.mPaint.setAlpha(alpha);
        }
    }

    @Override public void setColorFilter(@Nullable ColorFilter colorFilter) {
        if (mParams.mPaint != null) {
            mParams.mPaint.setColorFilter(colorFilter);
        }
    }

    @Override public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static class Builder {

        int mRectCount;
        float mRatio; //
        Paint mPaint;
        int mRectColor;

        public Builder() {
            mPaint = new Paint();
        }

        public Builder setRectCount(int rectCount) {
            mRectCount = rectCount;
            return this;
        }

        public Builder setRectColor(int color) {
            mRectColor = color;
            return this;
        }

        public Builder setRatio(float ratio) {
            mRatio = ratio;
            return this;
        }

        private void apply() {
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeWidth(1);
            mPaint.setColor(mRectColor);
        }

        public MusicBarDrawable build() {
            apply();
            return new MusicBarDrawable(new Params(mRectCount, mRatio, mPaint, mRectColor));
        }
    }

    static class Params {

        private int mRectCount;
        private float mRatio; // (gapWidth / rectWidth) = ratio
        private Paint mPaint;
        private int mRectColor;

        public Params(int mRectCount, float mRatio, Paint mPaint, int mRectColor) {
            this.mRectCount = mRectCount;
            this.mRatio = mRatio;
            this.mPaint = mPaint;
            this.mRectColor = mRectColor;
        }

        @Override public String toString() {
            return "Params{" +
                "mRectCount=" + mRectCount +
                ", mRatio=" + mRatio +
                ", mPaint=" + mPaint +
                ", mRectColor=" + mRectColor +
                '}';
        }
    }
}

package com.kumaj.loadingdrawable.export;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class ExportLoadingDrawable extends Drawable implements Animatable {

    private static final String TAG = "ExportLoadingDrawable";

    private static final float sDefaultTotalAngle = 360f;
    private static final float sDefaultStartAngle = -90f;

    private Params mParams;
    private final RectF mBounds = new RectF();
    private ValueAnimator mValueAnimator;

    private ExportLoadingDrawable(Params mParams) {
        this.mParams = mParams;
        setupAnimation();
    }

    private void setupAnimation() {
        //sweep angle
        mValueAnimator = ValueAnimator.ofFloat(0f, 360f);
        mValueAnimator.setInterpolator(mParams.mInterpolator);
        mValueAnimator.setDuration(5000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mParams.mSweepAngle = (float) animation.getAnimatedValue();
                invalidateSelf();
            }
        });
    }

    @Override protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float border = mParams.mStrokeWidth;
        float radius = mParams.mIndicatorRadius;
        mBounds.left = bounds.left + border / 2f + radius + .5f;
        mBounds.right = bounds.right - border / 2f - radius - .5f;
        mBounds.top = bounds.top + border / 2f + radius + .5f;
        mBounds.bottom = bounds.bottom - border / 2f - radius - .5f;
    }

    public RectF getDrawableBounds() {
        return mBounds;
    }

    public float getPercentage() {
        return mParams.mSweepAngle / sDefaultTotalAngle;
    }

    public float getProgress(){
        return mParams.mSweepAngle;
    }

    public void setProgress(@FloatRange(from = 0f, to = 360f) float sweepAngle) {
        mParams.mSweepAngle = sweepAngle;
        invalidateSelf();
    }

    @Override public void draw(@NonNull Canvas canvas) {
        final float centerX = getDrawableBounds().centerX();
        final float centerY = getDrawableBounds().centerY();
        final float radius = getDrawableBounds().width() / 2;
        mParams.mPaint.setStyle(Paint.Style.STROKE);
        mParams.mPaint.setColor(mParams.mBackgroundColor);
        canvas.drawCircle(centerX, centerY, radius, mParams.mPaint);
        mParams.mPaint.setColor(mParams.mForegroundColor);
        canvas.drawArc(getDrawableBounds(), sDefaultStartAngle, mParams.mSweepAngle, false,
            mParams.mPaint);
        final double angle = Math.toRadians(sDefaultStartAngle + mParams.mSweepAngle);
        final float cx = (float) (centerX + radius * Math.cos(angle));
        final float cy = (float) (centerY + radius * Math.sin(angle));
        mParams.mPaint.setStrokeWidth(mParams.mIndicatorRadius);
        mParams.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(cx, cy, mParams.mIndicatorRadius, mParams.mPaint);
    }

    @Override public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        mParams.mPaint.setAlpha(alpha);
    }

    @Override public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mParams.mPaint.setColorFilter(colorFilter);
    }

    @Override public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override public void start() {
        mValueAnimator.start();
    }

    @Override public void stop() {
        mValueAnimator.cancel();
    }

    @Override public boolean isRunning() {
        return false;
    }

    public static class Builder {
        private Paint mPaint;
        private float mStrokeWidth;
        private int mForegroundColor;
        private int mBackgroundColor;
        private float mIndicatorRadius;
        private float mSweepAngle;
        private Interpolator mInterpolator;

        public Builder() {
            mPaint = new Paint();
            mInterpolator = new AccelerateDecelerateInterpolator();
        }

        public Builder setStrokeWidth(float strokeWidth) {
            mStrokeWidth = strokeWidth;
            return this;
        }

        public Builder setForegroundColor(int color) {
            mForegroundColor = color;
            return this;
        }

        public Builder setBackgroundColor(int color) {
            mBackgroundColor = color;
            return this;
        }

        public Builder setIndicatorRadius(float indicatorRadius) {
            this.mIndicatorRadius = indicatorRadius;
            return this;
        }

        public Builder setSweepAngle(float sweepAngle) {
            this.mSweepAngle = sweepAngle;
            return this;
        }

        public Builder setInterpolator(Interpolator interpolator) {
            mInterpolator = interpolator;
            return this;
        }

        private void apply() {
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setColor(mBackgroundColor);
        }

        public ExportLoadingDrawable build() {
            apply();
            return new ExportLoadingDrawable(
                new Params(mPaint,
                    mForegroundColor,
                    mBackgroundColor,
                    mStrokeWidth,
                    mIndicatorRadius,
                    mSweepAngle,
                    mInterpolator));
        }
    }

    static class Params {
        Paint mPaint;
        int mForegroundColor;
        int mBackgroundColor;
        float mStrokeWidth;
        float mIndicatorRadius;
        float mSweepAngle;
        Interpolator mInterpolator;

        public Params(Paint mPaint,
                      int mForegroundColor,
                      int mBackgroundColor,
                      float mStrokeWidth,
                      float mIndicatorRadius,
                      float mSweepAngle,
                      Interpolator mInterpolator) {
            this.mPaint = mPaint;
            this.mForegroundColor = mForegroundColor;
            this.mBackgroundColor = mBackgroundColor;
            this.mStrokeWidth = mStrokeWidth;
            this.mIndicatorRadius = mIndicatorRadius;
            this.mInterpolator = mInterpolator;
        }
    }
}

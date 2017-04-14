package com.kumaj.loadingdrawable.octocat;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.kumaj.loadingdrawable.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OctoCatDrawable extends Drawable implements Animatable {

    private final String TAG = getClass().getSimpleName();
    private Paint mPaint;
    private Params mParams;
    private final RectF mBounds = new RectF();
    private final RectF mTempBounds = new RectF();

    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_ROUNDED = 1;

    private ValueAnimator mRotationAnimator;
    private ValueAnimator mSpreadAnimator;

    private float mRotationAngle = 0f;
    private float mSweepAngle;
    private int mColorIndex = 0;
    private float mScale;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ STYLE_NORMAL, STYLE_ROUNDED })
    public @interface Style {
    }


    public OctoCatDrawable(Params params) {
        this.mParams = params;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(params.strokeWidth);
        mPaint.setStrokeCap(params.style == STYLE_ROUNDED ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mPaint.setColor(params.colors[0]);
        //calculate the sweep angle
        mSweepAngle = (360f - mParams.section * mParams.IntervalAngle) / mParams.section;
        setupAnimation();
    }


    private void setupAnimation() {
        mSpreadAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
        mSpreadAnimator.setInterpolator(new AccelerateInterpolator());
        mSpreadAnimator.setDuration(1000);
        mSpreadAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mScale = animation.getAnimatedFraction();
            }
        });

        mRotationAnimator = ValueAnimator.ofFloat(0f, 360f);
        mRotationAnimator.setInterpolator(new LinearInterpolator());
        mRotationAnimator.setDuration(2000);
        mRotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                float rotateAngle = animation.getAnimatedFraction() * 360f;
                setRotationAngle(rotateAngle);
            }


        });
        mRotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotationAnimator.addListener(new Animator.AnimatorListener() {
            @Override public void onAnimationStart(Animator animation) {
                setDrawableColor(mParams.colors[0]);
                mColorIndex++;
            }


            @Override public void onAnimationEnd(Animator animation) {

            }


            @Override public void onAnimationCancel(Animator animation) {

            }


            @Override public void onAnimationRepeat(Animator animation) {
                int color = mParams.colors[mColorIndex % mParams.colors.length];
                setDrawableColor(color);
                mColorIndex++;
            }
        });


    }

    private void changeState() {}

    @Override public void draw(Canvas canvas) {
        mTempBounds.set(getDrawableBounds());
        mTempBounds.inset(mParams.strokeWidth, mParams.strokeWidth);
        mTempBounds.inset((1 - mScale) * mTempBounds.width(), (1 - mScale) * mTempBounds.height());
        Log.e(TAG,"scale = " + mScale);
        float px = getDrawableBounds().centerX();
        float py = getDrawableBounds().centerY();
        int scale = 2;
        int w = mParams.drawable.getIntrinsicWidth() / scale / 2;
        int h = mParams.drawable.getIntrinsicHeight() / scale / 2;
        canvas.save();
        canvas.rotate(mRotationAngle, px, py);
        for (int i = 0; i < mParams.section; i++) {
            canvas.drawArc(mTempBounds, 0, mSweepAngle, false, mPaint);
            canvas.rotate(mSweepAngle + mParams.IntervalAngle, px, py);
            int color = mParams.colors[i % mParams.colors.length];
            mPaint.setColor(color);
        }

        canvas.restore();
        canvas.translate(getDrawableBounds().centerX(), getDrawableBounds().centerY());
        mParams.drawable.setBounds(-w, -h, w, h); //l t r b
        mParams.drawable.draw(canvas);
    }


    @Override public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }


    @Override public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }


    @Override public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float border = mParams.strokeWidth;
        mBounds.left = bounds.left + border / 2f + .5f;
        mBounds.right = bounds.right - border / 2f - .5f;
        mBounds.top = bounds.top + border / 2f + .5f;
        mBounds.bottom = bounds.bottom - border / 2f - .5f;
    }


    public RectF getDrawableBounds() {
        return mBounds;
    }


    @Override public void start() {
        mSpreadAnimator.start();
        mRotationAnimator.start();
    }


    @Override public void stop() {
        mSpreadAnimator.cancel();
        mRotationAnimator.cancel();
    }


    @Override public boolean isRunning() {
        return false;
    }


    private void setRotationAngle(float angle) {
        mRotationAngle = angle;
        invalidateSelf();
    }


    private void setDrawableColor(int color) {
        Drawable drawable = DrawableCompat.wrap(mParams.drawable);
        DrawableCompat.setTint(drawable, color);
    }


    public static class Builder {
        private float mStrokeWidth;
        private int[] mColors;
        private int mSections;
        private float mIntervalAngle;
        private Drawable mOctoCatDrawable;
        @OctoCatDrawable.Style private int mStyle;


        public Builder(@NonNull Context context) {
            mStrokeWidth = context.getResources()
                .getDimension(R.dimen.octo_cat_default_stroke_width);
            mColors = new int[] { Color.BLUE };
            mSections = context.getResources().getInteger(R.integer.octo_cat_default_section);
            mIntervalAngle = context.getResources()
                .getInteger(R.integer.octo_cat_default_interval_angle);
            mOctoCatDrawable = context.getResources().getDrawable(R.drawable.ic_octo_cat);
            mStyle = STYLE_ROUNDED;
        }


        public Builder setStrokeWidth(float strokeWidth) {
            mStrokeWidth = strokeWidth;
            return this;
        }


        public Builder setColor(int color) {
            mColors = new int[] { color };
            return this;
        }


        public Builder setColors(int[] colors) {
            mColors = colors;
            return this;
        }


        public Builder setSection(int section) {
            mSections = section;
            return this;
        }


        public Builder setStyle(@OctoCatDrawable.Style int style) {
            mStyle = style;
            return this;
        }


        public Builder setIntervalAngle(float interval) {
            mIntervalAngle = interval;
            return this;
        }


        public Builder setDrawable(Drawable drawable) {
            mOctoCatDrawable = drawable;
            return this;
        }


        public OctoCatDrawable build() {
            return new OctoCatDrawable(
                new Params(mStrokeWidth, mColors, mSections, mIntervalAngle, mOctoCatDrawable,
                    mStyle));
        }
    }
}

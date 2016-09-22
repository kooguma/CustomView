package com.kumaj.customview.widget.OctoCatProgress;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.util.Log;
import com.kumaj.customview.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OctoCatDrawable extends Drawable implements Animatable {

    private final String TAG = getClass().getSimpleName();
    private Paint mPaint;
    private Bitmap mBitmap;
    private Params mParams;
    private final RectF mBounds = new RectF();

    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_ROUNDED = 1;


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

    }


    @Override public void draw(Canvas canvas) {
        canvas.drawLine(100,100,500,500,mPaint);
        canvas.drawRect(getDrawableBounds(),mPaint);
        Log.e(TAG,"bounds = " + getDrawableBounds().toString());
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
        Log.e(TAG,"onBound change = " + bounds.toString());
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

    }


    @Override public void stop() {

    }


    @Override public boolean isRunning() {
        return false;
    }

    public static class Builder {
        private float mStrokeWidth;
        private int[] mColors;
        private int mSections;
        private float mIntervalAngle;
        @OctoCatDrawable.Style private int mStyle;


        public Builder(@NonNull Context context) {
            mStrokeWidth = context.getResources()
                .getDimension(R.dimen.octo_cat_default_stroke_width);
            mColors = new int[]{Color.BLUE};
            mSections =
            mStyle = STYLE_ROUNDED;
        }

        public Builder setStrokeWidth(float strokeWidth){
            mStrokeWidth = strokeWidth;
            return this;
        }

        public Builder setColor(int color){
            mColors = new int[]{color};
            return this;
        }

        public Builder setColors(int[] colors){
            mColors = colors;
            return this;
        }

        public Builder setSection(int section){
            mSections = section;
            return this;
        }

        public Builder setStyle(@OctoCatDrawable.Style  int style){
            mStyle = style;
            return this;
        }

        public Builder setInterval(float interval){
            mIntervalAngle = interval;
            return this;
        }

        public OctoCatDrawable build(){
            return new OctoCatDrawable(new Params(mStrokeWidth,mColors,mSections,mStyle));
        }
    }
}

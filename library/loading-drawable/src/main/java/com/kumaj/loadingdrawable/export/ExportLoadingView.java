package com.kumaj.loadingdrawable.export;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.kumaj.loadingdrawable.R;

public class ExportLoadingView extends AppCompatImageView {

    private ExportLoadingDrawable mDrawable;

    public ExportLoadingView(Context context) {
        this(context, null);
    }

    public ExportLoadingView(Context context,
                             @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExportLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExportLoadingView,
            defStyleAttr,
            0);
        final int foregroundColor = a.getColor(
            R.styleable.ExportLoadingView_progressForegroundColor,
            res.getColor(R.color.export_loading_foreground_progress_color));
        final int backgroundColor = a.getColor(
            R.styleable.ExportLoadingView_progressBackgroundColor,
            res.getColor(R.color.export_loading_background_progress_color));
        final float strokeWidth = a.getDimension(R.styleable.ExportLoadingView_strokeWidth,
            res.getDimension(R.dimen.export_loading_stroke_width));
        final float indicatorRadius = a.getDimension(R.styleable.ExportLoadingView_indicatorRadius,
            res.getDimension(R.dimen.export_loading_stroke_width));
        final float sweepAngle = a.getDimension(R.styleable.ExportLoadingView_progressSweepAngle,
            res.getDimension(R.dimen.export_loading_progress_sweep_angle));

        mDrawable = new ExportLoadingDrawable.Builder()
            .setBackgroundColor(backgroundColor)
            .setForegroundColor(foregroundColor)
            .setStrokeWidth(strokeWidth)
            .setIndicatorRadius(indicatorRadius)
            .setSweepAngle(sweepAngle)
            .build();
        a.recycle();

        setBackgroundDrawable(mDrawable);
    }

    @Override public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    public void startAnimation() {
        if (mDrawable != null) {
            mDrawable.start();
        }
    }

    public void stopAnimation() {
        if (mDrawable != null) {
            mDrawable.stop();
        }
    }

    public void setProgress(@FloatRange(from = 0f, to = 1f) float progress) {
        mDrawable.setProgress(progress * 360);
    }
}

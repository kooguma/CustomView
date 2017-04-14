package com.kumaj.followbox;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;

public class FollowBox extends View implements Checkable {
    private static final String TAG = "FollowBox";

    //Tick
    private Path mTickPath;
    private Paint mTickPaint;
    private Point[] mTickPoints;

    private Paint mPaint;
    private Rect mRect;

    private int mWidth;
    private int mHeight;

    private float mScale;

    public FollowBox(Context context) {
        super(context);
    }

    public FollowBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FollowBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#3FBEA2"));
        mRect = new Rect();

        mTickPath = new Path();

        mTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTickPaint.setStyle(Paint.Style.STROKE);
        mTickPaint.setColor(Color.WHITE);
        mTickPaint.setStrokeWidth(10);

        mTickPoints = new Point[4];
        mTickPoints[0] = new Point();
        mTickPoints[1] = new Point();
        mTickPoints[2] = new Point();
        mTickPoints[3] = new Point();

        setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                startUnCheckedAnimation();
            }
        });
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mTickPoints[0].x = Math.round(0.26f * mWidth);
        mTickPoints[0].y = Math.round(0.50f * mHeight);
        mTickPoints[1].x = Math.round(0.42f * mWidth);
        mTickPoints[1].y = Math.round(0.66f * mHeight);
        // mTickPoints[2].x = Math.round(0.42f * mWidth);
        // mTickPoints[2].y = Math.round(0.68f * mHeight);
        // mTickPoints[3].x = Math.round(0.75f * mWidth);
        // mTickPoints[3].y = Math.round(0.31f * mHeight);

        mRect.top = 0;
        mRect.bottom = mHeight;
        mRect.left = 0;
        mRect.right = mWidth;
    }

    @Override protected void onDraw(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);

        Log.e("TAG","point[0] = " + mTickPoints[0]);
        Log.e("TAG","point[1] = " + mTickPoints[1]);
        // canvas.drawPoint(mTickPoints[0].x, mTickPoints[0].y, mTickPaint);
        //
        canvas.rotate(mScale * 45);
        // // canvas.rotate(mScale*45,50,50);
        canvas.scale((1 + mScale), 1);
        //
        //mTickPath.moveTo(mTickPoints[0].x, mTickPoints[0].y);
        //mTickPath.lineTo(mTickPoints[1].x, mTickPoints[1].y);
        //mTickPath.lineTo(mTickPoints[2].x, mTickPoints[2].y);
        //mTickPath.lineTo(mTickPoints[3].x, mTickPoints[3].y);

        // canvas.rotate(mScale * 45);
        // canvas.scale((1 + mScale), 1);
        mTickPath.moveTo(50, 50);
        mTickPath.lineTo(75, 75);

        canvas.drawPath(mTickPath, mTickPaint);
    }

    private void startUnCheckedAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(6000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                mScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override public void setChecked(boolean checked) {

    }

    @Override public boolean isChecked() {
        return false;
    }

    @Override public void toggle() {

    }
}

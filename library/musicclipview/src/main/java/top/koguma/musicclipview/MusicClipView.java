package top.koguma.musicclipview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MusicClipView extends View {

    private static final String TAG = "MusicClip";

    private static final int sDefaultMax = 100;

    private static final int PROGRESS_HEIGHT = 2;

    private int mWidth;
    private int mHeight;
    private float mPosX;
    private float mPosY;

    private int mProgressStartX;
    private int mProgressStartY;

    private int mProgressMax;
    private int mProgressCur;

    private int mProgressWidth;
    private int mProgressColor;
    private int mProgressSelectedColor;
    private boolean mIsRoundCorner;
    private Paint mProgressPaint;

    private Indicator mIndicatorLeft;
    private Indicator mIndicatorRight;
    private Indicator mIndicatorTouched;

    private Paint mIndicatorLeftPaint;
    private Paint mIndicatorRightPaint;
    private Path mLeftTrianglePath;
    private Path mRightTrianglePath;

    private Paint mDotPaint;
    private int mDotColor;
    private float mDotProgress;

    private Paint mTextPaint;

    private Drawable mIndicatorDrawable;
    private Drawable mDotDrawable;

    private IndicatorMoveListener mIndicatorMoveListener;

    public void setIndicatorMoveListener(IndicatorMoveListener listener) {
        this.mIndicatorMoveListener = listener;
    }

    public interface IndicatorMoveListener {
        // void onProgressChanged(float progress);
        void onLeftIndicatorMoving(float position1, float position2);
        void onRightIndicatorMoving(float position1, float position2);
        void onLeftIndicatorMoved(float position);
        void onRightIndicatorMoved(float position);
    }

    public MusicClipView(Context context) {
        super(context);
    }

    public MusicClipView(Context context,
                         @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs, 0);
    }

    public MusicClipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Resources res = getResources();
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MusicClipView,
            defStyleAttr, 0);
        mProgressColor = a.getColor(R.styleable.MusicClipView_progressbarColor,
                ContextCompat.getColor(context, R.color.music_clip_progress_color));
        mProgressSelectedColor = a.getColor(R.styleable.MusicClipView_progressbarSelectedColor,
                ContextCompat.getColor(context, R.color.music_clip_progress_selected_color));
        mDotColor = a.getColor(R.styleable.MusicClipView_dotColor,
                ContextCompat.getColor(context, R.color.music_clip_progress_dot_color));

        mProgressMax = a.getInteger(R.styleable.MusicClipView_progressMax, sDefaultMax);
        mIsRoundCorner = a.getBoolean(R.styleable.MusicClipView_roundCorner, false);

        a.recycle();

        mIndicatorDrawable = getResources().getDrawable(R.drawable.ic_music_clip_slip_bar);
        mDotDrawable = getResources().getDrawable(R.drawable.ic_music_clip_slip_dot);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeWidth(DeviceScreenUtils.dp2px(PROGRESS_HEIGHT, getContext()));
        if (mIsRoundCorner) {
            mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        mIndicatorLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorLeftPaint.setColor(Color.WHITE);
        mIndicatorLeftPaint.setStrokeWidth(2);

        mIndicatorRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorRightPaint.setColor(Color.WHITE);
        mIndicatorRightPaint.setStrokeWidth(2);

        final int radius = mIndicatorDrawable.getIntrinsicWidth()/2;

        mIndicatorLeft = new Indicator();
        mIndicatorLeft.setRadius(radius).setPaint(mIndicatorLeftPaint);
        mLeftTrianglePath = new Path();

        mIndicatorRight = new Indicator();
        mIndicatorRight.setRadius(radius).setPaint(mIndicatorRightPaint);
        mRightTrianglePath = new Path();

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setColor(mDotColor);
        mDotPaint.setStrokeWidth(10);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(getResources().getColor(R.color.text_color_tertiary));
        mTextPaint.setTextSize(32);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();

        mProgressWidth = mWidth - getPaddingLeft() - getPaddingRight() - mIndicatorLeft.radius * 2;
        mProgressStartX = getPaddingLeft() + mIndicatorLeft.radius;
        mProgressStartY = mHeight / 2;

        final int cxl = mProgressStartX + mIndicatorLeft.cx;
        final int cyl = mProgressStartY;
        mIndicatorLeft.setPivotX(cxl).setPivotY(cyl);

        final int cxr = mProgressStartX + mProgressWidth;
        final int cyr = mProgressStartY;
        mIndicatorRight.setPivotX(cxr).setPivotY(cyr);

    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        mPosX = event.getX();
        mPosY = event.getY();
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // TODO: 2017/3/31 重合的情况
                if (mIndicatorLeft.getRectF().contains(mPosX, mPosY)) {
                    //触摸左指示器
                    mIndicatorTouched = mIndicatorLeft;
                } else if (mIndicatorRight.getRectF().contains(mPosX, mPosY)) {
                    //触摸右指示器
                    mIndicatorTouched = mIndicatorRight;
                } else {
                    //触摸其它位置
                    mIndicatorTouched = null;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int cx = (int) (mPosX - mIndicatorLeft.radius - getPaddingLeft());
                final int cy = mProgressStartY;
                boolean shouldInvalidate = false;
                if (mIndicatorTouched != null) {
                    if (checkIndicatorPivotX(cx)) {
                        mIndicatorTouched.setPivotX(cx);
                        final float position1 =
                            (float) (mIndicatorTouched.cx - mIndicatorTouched.radius) /
                                mProgressWidth;
                        mDotProgress = position1 * 100;
                        if (mIndicatorMoveListener != null) {
                            float position2;
                            if (mIndicatorTouched == mIndicatorLeft) {
                                position2 =
                                    (float) (mIndicatorRight.cx - mIndicatorRight.radius) /
                                        mProgressWidth;
                                mIndicatorMoveListener.onLeftIndicatorMoving(position1, position2);
                            } else {
                                position2 =
                                    (float) (mIndicatorLeft.cx - mIndicatorLeft.radius) /
                                        mProgressWidth;
                                mIndicatorMoveListener.onRightIndicatorMoving(position1, position2);
                            }
                        }
                        shouldInvalidate = true;
                    }
                    if (checkIndicatorPivotY(cy)) {
                        mIndicatorTouched.setPivotY(cy);
                        shouldInvalidate = true;
                    }
                }
                if (shouldInvalidate) {
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIndicatorMoveListener != null && mIndicatorTouched != null) {
                    final float position =
                        (float) (mIndicatorTouched.cx - mIndicatorTouched.radius) /
                            mProgressWidth;
                    if (mIndicatorTouched == mIndicatorLeft) {
                        mIndicatorMoveListener.onLeftIndicatorMoved(position);
                    } else {
                        mIndicatorMoveListener.onRightIndicatorMoved(position);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mIndicatorTouched = null;
                break;
        }
        return true;
    }

    private boolean checkIndicatorPivotX(int cx) {
        if (mIndicatorTouched.cx == cx) { //无变化
            return false;
        } else {
            if (mIndicatorTouched == mIndicatorLeft) {
                if (cx < mProgressStartX) {     //左边界
                    return false;
                } else if (cx > mIndicatorRight.cx) { //右边界
                    return false;
                } else {
                    return true;
                }
            } else {
                if (cx > mProgressStartX + mProgressWidth) { //右边界
                    return false;
                } else if (cx < mIndicatorLeft.cx) {//左边界
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    private boolean checkIndicatorPivotY(int cy) {
        return cy != mIndicatorTouched.cy;
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mIndicatorDrawable.getIntrinsicHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawDot(canvas);
        drawIndicator(canvas);
    }

    //时间和滑块一起动
    private void drawTimeText(Canvas canvas) {
        final float textWidth = mTextPaint.measureText("00:01.02");

        final float posL = mIndicatorLeft.cx;
        final float y = mIndicatorLeft.cy - mIndicatorDrawable.getIntrinsicHeight() / 2 - 14;

        float cxl;
        if (posL < textWidth / 2) {
            cxl = mProgressStartX - mIndicatorLeft.radius;
        } else {
            cxl = posL - textWidth / 2;
        }
        canvas.drawText("00:01.02", cxl, y, mTextPaint);

        final float posR = mIndicatorRight.cx;
        float cxr;
        if (posR > (mProgressStartX + mProgressWidth + mIndicatorRight.radius - textWidth / 2)) {
            cxr = mProgressStartX + mProgressWidth + mIndicatorRight.radius - textWidth;
        } else {
            cxr = posR - textWidth / 2;
        }
        canvas.drawText("00:01.02", cxr, y, mTextPaint);

    }

    private void drawDot(Canvas canvas) {
        // TODO: 2017/5/15 dot position
        final float scale = mDotProgress / sDefaultMax;
        final float x = mProgressStartX + scale * mProgressWidth;
        final float y = mProgressStartY;
        drawDrawable(canvas, mDotDrawable, x, y);
    }

    private void drawDrawable(Canvas canvas, Drawable drawable, float x, float y) {
        canvas.save();
        canvas.translate(x, y);
        final int height = drawable.getIntrinsicHeight();
        final int width = drawable.getIntrinsicWidth();
        drawable.setBounds(-width / 2, -height / 2, width / 2, height / 2);
        drawable.draw(canvas);
        canvas.restore();
    }

    private void drawProgress(Canvas canvas) {

        //1
        mProgressPaint.setColor(mProgressSelectedColor);
        canvas.drawLine(mProgressStartX, mProgressStartY, mIndicatorLeft.cx, mProgressStartY,
            mProgressPaint);
        //2
        mProgressPaint.setColor(mProgressColor);
        canvas.drawLine(mIndicatorLeft.cx, mProgressStartY, mIndicatorRight.cx, mProgressStartY,
            mProgressPaint);
        //3
        mProgressPaint.setColor(mProgressSelectedColor);
        canvas.drawLine(mIndicatorRight.cx, mProgressStartY, mProgressStartX + mProgressWidth,
            mProgressStartY, mProgressPaint);
    }

    private void drawIndicator(Canvas canvas) {
        drawIndicatorLeft(canvas);
        drawIndicatorRight(canvas);
    }

    private void drawIndicatorLeft(Canvas canvas) {
        drawIndicator(canvas, mIndicatorLeft, mLeftTrianglePath);
    }

    private void drawIndicatorRight(Canvas canvas) {
        drawIndicator(canvas, mIndicatorRight, mRightTrianglePath);
    }

    private void drawIndicator(Canvas canvas, Indicator indicator, Path path) {
        drawDrawable(canvas, mIndicatorDrawable, indicator.cx, indicator.cy);
    }

    public void setDotProgress(@FloatRange(from = 0f, to = 100f) float dotProgress) {
        mDotProgress = dotProgress;
        postInvalidate();
    }

    public float getRightIndicatorProgress() {
        return getIndicatorProgress(mIndicatorRight);
    }

    public float getLeftIndicatorProgress() {
        return getIndicatorProgress(mIndicatorLeft);
    }

    private float getIndicatorProgress(Indicator indicator) {
        return indicator == null ? 0 : indicator.cx - indicator.radius;
    }

    class Indicator {
        int radius;
        int cx;
        int cy;

        Path path;
        Paint paint;

        public Indicator() {
        }

        public Indicator(int radius, int cx, int cy, Paint paint) {
            this.radius = radius;
            this.cx = cx;
            this.cx = cy;
            this.paint = paint;
        }

        public void draw(Canvas canvas) {
            drawCircle(canvas);
            drawTriangle(canvas);
        }

        private void drawCircle(Canvas canvas) {
            canvas.drawCircle(cx, cy, radius, paint);
        }

        private void drawTriangle(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        public RectF getRectF() {
            final int w = (int) (2.0f * mIndicatorDrawable.getIntrinsicWidth() / 2);
            final int h = (int) (2.0f * mIndicatorDrawable.getIntrinsicHeight() / 2);
            return new RectF(cx - w, cy - h, cx + w, cy + h);
        }

        public Indicator setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Indicator setPivotX(int cx) {
            this.cx = cx;
            return this;
        }

        public Indicator setPivotY(int cy) {
            this.cy = cy;
            return this;
        }

        public Indicator setPaint(Paint paint) {
            this.paint = paint;
            return this;
        }

        public Indicator setPath(Path path) {
            this.path = path;
            return this;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Indicator indicator = (Indicator) o;

            if (radius != indicator.radius) return false;
            if (cx != indicator.cx) return false;
            if (cy != indicator.cy) return false;
            if (path != null ? !path.equals(indicator.path) : indicator.path != null) return false;
            return paint != null ? paint.equals(indicator.paint) : indicator.paint == null;

        }

        @Override public int hashCode() {
            int result = radius;
            result = 31 * result + cx;
            result = 31 * result + cy;
            result = 31 * result + (path != null ? path.hashCode() : 0);
            result = 31 * result + (paint != null ? paint.hashCode() : 0);
            return result;
        }
    }
}

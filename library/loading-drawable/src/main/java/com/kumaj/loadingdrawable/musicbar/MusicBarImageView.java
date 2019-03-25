package com.kumaj.loadingdrawable.musicbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import com.kumaj.loadingdrawable.R;

public class MusicBarImageView extends AppCompatImageView {

    private MusicBarDrawable mDrawable;

    public MusicBarImageView(Context context) {
        this(context, null);
    }

    public MusicBarImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicBarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Resources res = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MusicBarImageView,
            defStyleAttr, 0);
        final int barColor = a.getColor(R.styleable.MusicBarImageView_barColor,
            res.getColor(R.color.music_bar_color));
        final int barCount = a.getColor(R.styleable.MusicBarImageView_barCount,
            res.getInteger(R.integer.music_bar_count));
        final float ratio = a.getFloat(R.styleable.MusicBarImageView_ratio,
            Float.parseFloat(res.getString(R.string.music_bar_ratio)));

        mDrawable = new MusicBarDrawable.Builder()
            .setRectColor(barColor)
            .setRectCount(barCount)
            .setRatio(ratio)
            .build();

        a.recycle();

        setBackground(mDrawable);
    }

}

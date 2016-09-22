package com.kumaj.customview.widget.OctoCatProgress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import com.kumaj.customview.R;

public class OctoCatProgress extends ProgressBar {

    public OctoCatProgress(Context context) {
        this(context, null);
    }


    public OctoCatProgress(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.style);
    }


    public OctoCatProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Resources res = getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OctoCatProgress,
            defStyleAttr, 0);

        final int color = a.getColor(R.styleable.OctoCatProgress_color,
            res.getColor(R.color.color_default));
        final int colorsId = a.getResourceId(R.styleable.OctoCatProgress_colos, 0);
        final float strokeWidth = a.getDimension(R.styleable.OctoCatProgress_strokeWidth,
            res.getDimension(R.dimen.octo_cat_default_stroke_width));
        final int section = a.getInteger(R.styleable.OctoCatProgress_section,
            res.getInteger(R.integer.octo_cat_default_section));
        final float intervalAngle = a.getFloat(R.styleable.OctoCatProgress_intervalAngle,
            res.getInteger(R.integer.octo_cat_default_interval_angle));
        a.recycle();

        int[] colors = null;
        if (colorsId != 0) {
            colors = res.getIntArray(colorsId);
        }
        Drawable indeterminateDrawable;
        OctoCatDrawable.Builder builder = new OctoCatDrawable.Builder(context)
            .setStrokeWidth(strokeWidth)
            .setSection(section)
            .setInterval(intervalAngle);

        if (colors != null && colors.length > 0) {
            builder.setColors(colors);
        } else {
            builder.setColor(color);
        }

        indeterminateDrawable = builder.build();
        setIndeterminateDrawable(indeterminateDrawable);
    }

}

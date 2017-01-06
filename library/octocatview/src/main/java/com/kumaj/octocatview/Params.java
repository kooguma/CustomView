package com.kumaj.octocatview;

import android.graphics.drawable.Drawable;

public class Params {

    final float strokeWidth;
    final int[] colors;
    final int section;
    final float IntervalAngle;
    final Drawable drawable;
    @OctoCatDrawable.Style final int style;


    public Params(float strokeWidth, int[] colors, int section, float intervalAngle, Drawable drawable,
                  @OctoCatDrawable.Style int style) {
        this.strokeWidth = strokeWidth;
        this.colors = colors;
        this.section = section;
        this.IntervalAngle = intervalAngle;
        this.drawable = drawable;
        this.style = style;
    }
}

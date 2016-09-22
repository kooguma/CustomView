package com.kumaj.customview.widget.OctoCatProgress;

public class Params {

    final float strokeWidth;
    final int[] colors;
    final int section;
    @OctoCatDrawable.Style final int style;


    public Params(float strokeWidth, int[] colors,int section, @OctoCatDrawable.Style int style) {
        this.strokeWidth = strokeWidth;
        this.colors = colors;
        this.section = section;
        this.style = style;
    }
}

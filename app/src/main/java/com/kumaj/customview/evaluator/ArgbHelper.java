package com.kumaj.customview.evaluator;

import android.animation.ArgbEvaluator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.view.animation.LinearInterpolator;

public class ArgbHelper {

    private static final String TAG = "ArgbHelper";
    private static final ArgbHelper instance = new ArgbHelper();
    private TimeInterpolator mInterpolator;
    private TypeEvaluator mEvaluator;


    private ArgbHelper() {
        mInterpolator = new LinearInterpolator();
        mEvaluator = new ArgbEvaluator();
    }


    public static ArgbHelper getInstance() {
        return instance;
    }


    public ArgbHelper setInterpolator(TimeInterpolator value) {
        mInterpolator = value;
        return instance;
    }


    public ArgbHelper setEvaluator(TypeEvaluator value) {
        mEvaluator = value;
        return instance;
    }


    public TimeInterpolator getInterpolator() {
        return mInterpolator;
    }


    public TypeEvaluator getTypeEvaluator() {
        return mEvaluator;
    }


    @SuppressWarnings("unchecked")
    public int[] getValues(int scale, Object startValue, Object endValue) {
        int[] colors = new int[scale];
        float step = 1.0f / scale * 1.0f;
        int index = 0;
        for (float input = 0; input < 1; input += step) {
            float fraction = mInterpolator.getInterpolation(input);
            if (index == scale - 1) { //handle the last one,cause the error float provided
                colors[index] = (int) mEvaluator.evaluate(1, startValue, endValue);
                break;
            }
            colors[index++] = (int) mEvaluator.evaluate(fraction, startValue, endValue);
        }
        return colors;
    }


    public int[] getValues(int scale, Object... values) {
        int section = scale / (values.length - 1);
        boolean isInt = (scale % (values.length - 1)) == 0;
        int[][] colors = new int[values.length - 1][section];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = getValues(section, values[i], values[i + 1]);
        }

        int[] newColors = new int[scale];
        for (int j = 0; j < colors.length; j++) {
            for (int k = 0; k < colors[j].length; k++) {
                newColors[j * section + k] = colors[j][k];
            }
        }
        if (!isInt) { // handle the last one
            newColors[scale - 1] = (int) values[values.length - 1];
        }
        return newColors;
    }

}

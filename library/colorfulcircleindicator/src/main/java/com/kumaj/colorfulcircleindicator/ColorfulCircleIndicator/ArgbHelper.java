package com.kumaj.colorfulcircleindicator.ColorfulCircleIndicator;

import android.animation.ArgbEvaluator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.graphics.Color;
import android.view.animation.LinearInterpolator;

public final class ArgbHelper {

    private static final String TAG = "ArgbHelper";
    private static final TypeEvaluator EVALUATOR = new ArgbEvaluator();

    public static int[] getValues(int scale, Object startValue, Object endValue, TimeInterpolator interpolator) {
        int[] colors = new int[scale];
        float step = 1.0f / scale * 1.0f;
        int index = 0;
        for (float input = 0; input < 1; input += step) {
            float fraction = interpolator.getInterpolation(input);
            if (index == scale - 1) { //handle the last one,cause the error float provided
                colors[index] = (int) EVALUATOR.evaluate(1, startValue, endValue);
                break;
            }
            colors[index++] = (int) EVALUATOR.evaluate(fraction, startValue, endValue);
        }
        return colors;
    }


    @SuppressWarnings("unchecked")
    public static int[] getValues(int scale, Object startValue, Object endValue) {
        return getValues(scale, startValue, endValue, new LinearInterpolator());
    }


    public static int[] getValues(int scale, Object... values) {
        if (values.length == 1) {
            return new int[] { Color.RED };
        }

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

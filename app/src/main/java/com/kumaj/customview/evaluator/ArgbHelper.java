package com.kumaj.customview.evaluator;

import android.animation.ArgbEvaluator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.nfc.Tag;
import android.util.Log;
import android.view.animation.LinearInterpolator;

public class ArgbHelper implements TypeEvaluator, TimeInterpolator {

    private static final String TAG = "ArgbHelper";

    private TimeInterpolator mInterpolator;
    private TypeEvaluator mEvaluator;


    public ArgbHelper() {
        mInterpolator = new LinearInterpolator();
        mEvaluator = new ArgbEvaluator();
    }

    public void setInterpolator(TimeInterpolator value) {
        mInterpolator = value;
    }

    public void setEvaluator(TypeEvaluator value) {
        mEvaluator = value;
    }

    public TimeInterpolator getInterpolator() {
        return mInterpolator;
    }

    public TypeEvaluator getTypeEvaluator() {
        return mEvaluator;
    }

    @Override
    public float getInterpolation(float input) {
        return mInterpolator.getInterpolation(input);
    }

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        fraction = mInterpolator.getInterpolation(fraction);
        return mEvaluator.evaluate(fraction, startValue, endValue);
    }

    //make 70 values into 100 values,we should evaluate 30 values and insert it into the 70 values
    public int[] getValues(int scale, int... values) {
        if (values == null || values.length == 0) return values;

        float step = (1.0f * scale) / (1.0f * values.length);

        int[] colors = new int[scale];

        float fraction = 1.0f / (1.0f * values.length);

        //depart the values into serial parts 100 / 3 = 33.3
        //section = 33
        int section = (int) Math.floor((1.0d * scale) / (1.0d * values.length));

//
//        for (int i = 0; i < values.length; i++) {
//            for (int j = 0 ; j <  ;j++)
//            colors[index] = (int) evaluate((j * 1.0f) * fraction, values[i], values[i + 1]);
//
//        }

        return colors;
    }
}

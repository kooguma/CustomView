package com.kumaj.customview.utils;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

public class DrawableUtil {

    public static void setImageWithTint(ImageView view, Drawable drawable, ColorStateList tint, PorterDuff.Mode tintMode) {
        if (drawable != null) {
            if (tint != null) {
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTintList(drawable, tint);
                if (tintMode != null) {
                    DrawableCompat.setTintMode(drawable, tintMode);
                }
            }
            view.setImageDrawable(drawable);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (tint != null) {
                view.setImageTintList(tint);
            }
            if (tintMode != null) {
                view.setImageTintMode(tintMode);
            }
        }
    }
}

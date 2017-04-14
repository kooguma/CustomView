package com.kumaj.customview.sample;

import android.animation.Animator;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import com.kumaj.customview.R;
import com.kumaj.customview.test.Rotate3dAnimation;
import com.kumaj.customview.test.TranslateAnimation;

import static android.view.animation.Animation.REVERSE;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mImageView = (ImageView) findViewById(R.id.image);
        Button btnRotate = (Button) findViewById(R.id.btn_rotate);
        Button btnTranslate = (Button) findViewById(R.id.btn_translate);
        btnRotate.setOnClickListener(this);
        btnTranslate.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rotate:
                final float centerX = mImageView.getX() + mImageView.getWidth() / 2;
                final float centerY = mImageView.getY() + mImageView.getHeight() / 2;
                Log.e("TAG", "centerX = " + centerX + " centerY = " + centerY);
                Log.e("TAG", "getPivotX = " + mImageView.getPivotX() + " getPivotY = " +
                    mImageView.getPivotY());
                Animation animation1 = new Rotate3dAnimation(0, 360,
                    mImageView.getPivotX(),
                    mImageView.getPivotY(),
                    500,
                    false);
                animation1.setDuration(5000);
                animation1.setRepeatCount(100);
                mImageView.startAnimation(animation1);
                break;
            case R.id.btn_translate:
                Animation animation2 = new TranslateAnimation(1,1,0);
                animation2.setDuration(3000);
                animation2.setRepeatCount(1);
                mImageView.startAnimation(animation2);
                break;
        }
    }
}

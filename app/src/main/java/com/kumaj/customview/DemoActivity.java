package com.kumaj.customview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kumaj.customview.widget.ColorfulCircleIndicator;
import com.kumaj.customview.widget.SimpleOnSeekBarChangeListener;

public class DemoActivity extends AppCompatActivity implements ColorfulCircleIndicator.OnDialViewChangeListener{

    private static final String TAG = "DemoActivity";

    private TextView mTextProgress;
    private ColorfulCircleIndicator mColorfulCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mTextProgress = (TextView) findViewById(R.id.text_progress);
        mColorfulCircleIndicator = (ColorfulCircleIndicator) findViewById(R.id.dial_view);
        mColorfulCircleIndicator.setDialViewChangeListener(this);
        mColorfulCircleIndicator.setColors(Color.RED,Color.YELLOW,Color.BLUE,Color.GREEN);
        ColorfulCircleIndicator.ColorBuilder builder = new ColorfulCircleIndicator.ColorBuilder();
        builder.setColors(0.5f,Color.RED,Color.YELLOW,new AccelerateInterpolator());
        builder.setColors(0.3f,Color.YELLOW,Color.BLUE,new DecelerateInterpolator());
        builder.setColors(0.2f,Color.BLUE,Color.GREEN);
        mColorfulCircleIndicator.setColors2(builder.create());

        SeekBar progressArcPaddingArc = (SeekBar) findViewById(R.id.progressArcPadding);
        progressArcPaddingArc.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColorfulCircleIndicator.setProgressArcPadding(progress);
                mColorfulCircleIndicator.invalidate();
            }
        });

        SeekBar progressBackgroundArcWidth = (SeekBar) findViewById(R.id.progressBgArcWidth);
        progressBackgroundArcWidth.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColorfulCircleIndicator.setProgressBgArcWidth(progress);
                mColorfulCircleIndicator.invalidate();
            }
        });

        SeekBar progressForegroundArcWidth = (SeekBar) findViewById(R.id.progressFgArcWidth);
        progressForegroundArcWidth.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColorfulCircleIndicator.setProgressFgArcWidth(progress);
                mColorfulCircleIndicator.invalidate();
            }
        });
        
        SeekBar pgBgArcStartAngel = (SeekBar) findViewById(R.id.PgBgArcStartAngle);
        pgBgArcStartAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColorfulCircleIndicator.setProgressBgStartAngle(progress);
                mColorfulCircleIndicator.invalidate();
            }
        });

        SeekBar pgBgArcSweepAngel = (SeekBar) findViewById(R.id.PgBgSweepAngle);
        pgBgArcSweepAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColorfulCircleIndicator.setProgressBgSweepAngle(progress);
                mColorfulCircleIndicator.invalidate();
            }
        });

        SeekBar pgFgArcStartAngel = (SeekBar) findViewById(R.id.PgFgStartAngle);
        pgFgArcStartAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColorfulCircleIndicator.setProgressFgStartAngle(progress);
                mColorfulCircleIndicator.invalidate();
            }
        });

        SeekBar pgFgArcSweepAngel = (SeekBar) findViewById(R.id.PgFgSweepAngle);
        pgFgArcSweepAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mColorfulCircleIndicator.setProgressFgSweepAngle(progress);
                mColorfulCircleIndicator.invalidate();
            }
        });

    }

    @Override
    public void onStartChange(ColorfulCircleIndicator colorfulCircleIndicator) {

    }

    @Override
    public void onProgressUpdate(ColorfulCircleIndicator colorfulCircleIndicator) {
        String percentage =  String.format("%.2f", colorfulCircleIndicator.getPercentage()*100);
        mTextProgress.setTextColor(colorfulCircleIndicator.getColor());
        mTextProgress.setText(getString(R.string.dial_view_percentage,percentage));
    }

    @Override
    public void onBeyondProgress(ColorfulCircleIndicator colorfulCircleIndicator) {
        mTextProgress.setText("out");
    }

    @Override
    public void onStopChange(ColorfulCircleIndicator colorfulCircleIndicator) {

    }
}

package com.kumaj.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kumaj.customview.widget.DialView;
import com.kumaj.customview.widget.SimpleOnSeekBarChangeListener;

public class DemoActivity extends AppCompatActivity implements DialView.OnDialViewChangeListener{

    private static final String TAG = "DemoActivity";

    private TextView mTextProgress;
    private DialView mDialView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mTextProgress = (TextView) findViewById(R.id.text_progress);
        mDialView = (DialView) findViewById(R.id.dial_view);
        mDialView.setDialViewChangeListener(this);

        SeekBar progressArcPaddingArc = (SeekBar) findViewById(R.id.progressArcPadding);
        progressArcPaddingArc.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDialView.setProgressArcPadding(progress);
                mDialView.invalidate();
            }
        });

        SeekBar progressBackgroundArcWidth = (SeekBar) findViewById(R.id.progressBgArcWidth);
        progressBackgroundArcWidth.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDialView.setProgressBgArcWidth(progress);
                mDialView.invalidate();
            }
        });

        SeekBar progressForegroundArcWidth = (SeekBar) findViewById(R.id.progressFgArcWidth);
        progressForegroundArcWidth.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDialView.setProgressFgArcWidth(progress);
                mDialView.invalidate();
            }
        });
        
        SeekBar pgBgArcStartAngel = (SeekBar) findViewById(R.id.PgBgArcStartAngle);
        pgBgArcStartAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDialView.setProgressBgStartAngle(progress);
                mDialView.invalidate();
            }
        });

        SeekBar pgBgArcSweepAngel = (SeekBar) findViewById(R.id.PgBgSweepAngle);
        pgBgArcSweepAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDialView.setProgressBgSweepAngle(progress);
                mDialView.invalidate();
            }
        });

        SeekBar pgFgArcStartAngel = (SeekBar) findViewById(R.id.PgFgStartAngle);
        pgFgArcStartAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDialView.setProgressFgStartAngle(progress);
                mDialView.invalidate();
            }
        });

        SeekBar pgFgArcSweepAngel = (SeekBar) findViewById(R.id.PgFgSweepAngle);
        pgFgArcSweepAngel.setOnSeekBarChangeListener(new SimpleOnSeekBarChangeListener() {
            @Override
            protected void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDialView.setProgressFgSweepAngle(progress);
                mDialView.invalidate();
            }
        });

    }

    @Override
    public void onStartChange(DialView dialView) {

    }

    @Override
    public void onProgressUpdate(DialView dialView) {
        String percentage =  String.format("%.2f", dialView.getPercentage()*100);
        mTextProgress.setText(getString(R.string.dial_view_percentage,percentage));
    }

    @Override
    public void onBeyondProgress(DialView dialView) {
        mTextProgress.setText("out");
    }

    @Override
    public void onStopChange(DialView dialView) {

    }
}

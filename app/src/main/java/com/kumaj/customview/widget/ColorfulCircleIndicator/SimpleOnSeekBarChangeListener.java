package com.kumaj.customview.widget.ColorfulCircleIndicator;

import android.widget.SeekBar;

public abstract class SimpleOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        onSimpleProgressChanged(seekBar,progress,fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    protected abstract void onSimpleProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
}

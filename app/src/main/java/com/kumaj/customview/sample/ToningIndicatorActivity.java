package com.kumaj.customview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.kumaj.customview.R;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import top.koguma.toningindicator.ToningIndicator;

public class ToningIndicatorActivity extends AppCompatActivity {

    private ToningIndicator indicator;
    private Disposable mDisposable;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toning_indicator);
        indicator = (ToningIndicator) findViewById(R.id.indicator);
        mDisposable = Flowable
            .interval(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(aLong -> {
                //随机正负号
                double sign = Math.random() > 0.5 ? 1d : -1d;
                //随机频率 -150 到 150
                double frequency = sign * Math.random() * 150;
                indicator.setDeltaFrequency((float) frequency);
            })
            .subscribe();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}

package com.kumaj.customview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.kumaj.customview.R;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;
import top.koguma.musicclipview.MusicClipView;

public class MusicClipViewActivity extends AppCompatActivity {

    private MusicClipView musicClipView;
    private Disposable mDisposable;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_clip_view);
        musicClipView = (MusicClipView) findViewById(R.id.music_clip_view);
        mDisposable = Flowable
            .interval(10, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(aLong -> {
                musicClipView.setDotProgress(aLong / 50.0f);
            })
            .subscribe();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}

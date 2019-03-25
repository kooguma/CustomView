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

    private MusicClipView musicClipView1;
    private MusicClipView musicClipView2;
    private MusicClipView musicClipView3;

    private Disposable mDisposable;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_clip_view);
        musicClipView1 = (MusicClipView) findViewById(R.id.music_clip_view_1);
        musicClipView2 = (MusicClipView) findViewById(R.id.music_clip_view_2);
        musicClipView3 = (MusicClipView) findViewById(R.id.music_clip_view_3);

        mDisposable = Flowable
            .interval(10, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(aLong -> {
                musicClipView1.setDotProgress(aLong / 25.0f);
                musicClipView2.setDotProgress(aLong / 50.0f);
                musicClipView3.setDotProgress(aLong / 75.0f);

            })
            .subscribe();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}

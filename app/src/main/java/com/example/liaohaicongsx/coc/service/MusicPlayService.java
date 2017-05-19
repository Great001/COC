package com.example.liaohaicongsx.coc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.liaohaicongsx.coc.view.MusicPlayWindow;

/**
 * Author Hancher
 * Date 2017/05/08
 * Contact liaohaicongsx@gomo.com
 */
public class MusicPlayService extends Service {

    public static final String MUSIC_PATH = "music_path";
    private String mPath;
    /**
     * 音乐播放悬浮窗
     */
    private MusicPlayWindow mMusicPlayWindow;

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicPlayWindow = new MusicPlayWindow(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPath = intent.getStringExtra(MUSIC_PATH);
        mMusicPlayWindow.startPlayMusic(mPath);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mMusicPlayWindow.release();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
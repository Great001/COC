package com.example.liaohaicongsx.coc.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.DimenUtil;

import java.io.IOException;


/**
 * created by Liaohaicongsx on 2017/05/08
 */
public class MusicPlayService extends Service {

    public static final String BROADCAST_ACTION = "com.example.liaohaicongsx.coc.service.MusicPlayService";

    public static final String MUSIC_PATH = "music_path";

    private String mPath;
    private MediaPlayer mMediaPlayer;

    private WindowManager mWm;
    private WindowManager.LayoutParams mWindowParams;
    private ImageView mIvMusic;

    private MyBroadcastReceiver mReceiver;

    private boolean isFwShow = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPath = intent.getStringExtra(MUSIC_PATH);
        RegisterBroadcastReceiver();  //注册广播监听
        initMusicPlayFw();  //初始化音乐播放悬浮窗

        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(this, Uri.parse(mPath));
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    showFw();  //显示悬浮窗
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                    hideFw();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void initMusicPlayFw() {
        mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        mIvMusic = new ImageView(this);
        mIvMusic.setClickable(true);
        mIvMusic.setImageResource(R.drawable.music_play);
        mIvMusic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DimenUtil.dp2px(this, 50), DimenUtil.dp2px(this, 50));
        mIvMusic.setLayoutParams(params);

        mIvMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
            }
        });

//        mIvMusic.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int x = (int) event.getRawX();
//                int y = (int) event.getRawY();
//                windowParams.x = x;
//                windowParams.y = DimenUtil.getScreenHeight(MusicPlayService.this) - y;
//                windowManager.updateViewLayout(v,windowParams);
//                return false;
//            }
//        });

        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowParams.width = DimenUtil.dp2px(this, 40);
        mWindowParams.height = DimenUtil.dp2px(this, 40);
        mWindowParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        mWindowParams.x = DimenUtil.dp2px(this, 20);
        mWindowParams.y = DimenUtil.dp2px(this, 60);
    }

    public void showFw() {
        if (!isFwShow) {
            mWm.addView(mIvMusic, mWindowParams);
            isFwShow = true;
        }
    }

    public void hideFw() {
        if (isFwShow) {
            mWm.removeView(mIvMusic);
            isFwShow = false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPath = intent.getStringExtra(MUSIC_PATH);
            startPlay();
        }
    }

    public void RegisterBroadcastReceiver() {
        mReceiver = new MyBroadcastReceiver();
        registerReceiver(mReceiver, new IntentFilter(BROADCAST_ACTION));
    }

    private void startPlay() {
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            mMediaPlayer = new MediaPlayer();  //必须这样，否则就报IllegalStateException
            mMediaPlayer.setDataSource(mPath);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    showFw();
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                    hideFw();
                }
            });
        } catch (IOException e) {

        }
    }

    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        hideFw();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
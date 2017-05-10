package com.example.liaohaicongsx.coc.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.DimenUtil;

import java.io.IOException;


/**
 * created by Liaohaicongsx on 2017/05/08
 */
public class MusicPlayService extends Service {

    public static final String BROADCAST_ACTION = "com.example.liaohaicongsx.coc.service.MusicPlayService.play";

    public static final String MUSIC_PATH = "music_path";

    public static final int MSG_PROGRESS_UPDATE = 1010;
    public static final int PROGRESS_UPDATE_INTERVAL = 1000;

    public static final int PROGRESS_PADDING = 8;

    private String mPath;
    private MediaPlayer mMediaPlayer;

    private WindowManager mWm;
    private WindowManager.LayoutParams mMusicViewParams;
    private ImageView mIvMusic;
    private ShapeDrawable mProgressShape;

    private MusicPlayReceiver mReceiver;

    private boolean isFwShow = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PROGRESS_UPDATE:
                    updateProgress();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


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
                    showProgress();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                    hideFw();
                    handler.removeMessages(MSG_PROGRESS_UPDATE);
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
        mIvMusic.setImageResource(R.drawable.music_playing);
        mIvMusic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mProgressShape = new ShapeDrawable();
        mIvMusic.setBackgroundDrawable(mProgressShape);

//        就算设置了也无效，具体原因待查
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DimenUtil.dp2px(this, 30), DimenUtil.dp2px(this, 30));
//        mIvMusic.setLayoutParams(params);

        mIvMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mIvMusic.setImageResource(R.drawable.music_pause);
                } else {
                    mMediaPlayer.start();
                    mIvMusic.setImageResource(R.drawable.music_playing);
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

        mMusicViewParams = new WindowManager.LayoutParams();
        mMusicViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mMusicViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mMusicViewParams.format = PixelFormat.RGBA_8888;
        mMusicViewParams.width = DimenUtil.dp2px(this, 40);
        mMusicViewParams.height = DimenUtil.dp2px(this, 40);
        mMusicViewParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        mMusicViewParams.x = DimenUtil.dp2px(this, 20);
        mMusicViewParams.y = DimenUtil.dp2px(this, 60);
    }

    public void showFw() {
        if (!isFwShow) {
            mWm.addView(mIvMusic, mMusicViewParams);
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

    class MusicPlayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPath = intent.getStringExtra(MUSIC_PATH);
            startPlay();
        }
    }

    public void RegisterBroadcastReceiver() {
        mReceiver = new MusicPlayReceiver();
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
                    //播放完成，停止进度更新
                    handler.removeMessages(MSG_PROGRESS_UPDATE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showProgress() {
        handler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 1000);
    }

    public void updateProgress() {
        int duration = mMediaPlayer.getDuration();
        int currentPos = mMediaPlayer.getCurrentPosition();
        final int sweepAngel = (currentPos * 360) / duration;
        if (mProgressShape != null) {
            mProgressShape.setShape(new Shape() {
                @Override
                public void draw(Canvas canvas, Paint paint) {
                    int left = mIvMusic.getLeft() + PROGRESS_PADDING;
                    int right = mIvMusic.getRight() - PROGRESS_PADDING;
                    int top = mIvMusic.getTop() + PROGRESS_PADDING;
                    int bottom = mIvMusic.getBottom() - PROGRESS_PADDING;
                    RectF rect = new RectF(left, top, right, bottom);

                    paint.setColor(getResources().getColor(R.color.sky_blue));
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeCap(Paint.Cap.SQUARE);
                    paint.setStrokeWidth(16);

                    canvas.drawArc(rect, -90, sweepAngel, false, paint);
                }
            });
            mIvMusic.setBackgroundDrawable(mProgressShape);
            if (sweepAngel <= 360) {
                handler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, PROGRESS_UPDATE_INTERVAL);
            }
        }
    }


    @Override
    public void onDestroy() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
        hideFw();
        handler.removeMessages(MSG_PROGRESS_UPDATE);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
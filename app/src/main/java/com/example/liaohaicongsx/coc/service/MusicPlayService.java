package com.example.liaohaicongsx.coc.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
 * Author Hancher
 * Date 2017/05/08
 * Contact liaohaicongsx@gomo.com
 */
public class MusicPlayService extends Service {

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

    private boolean isFwShow = false;
    private boolean isEnd;

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
    public void onCreate() {
        super.onCreate();
        initMusicPlayFw();
        mMediaPlayer = new MediaPlayer();
        isEnd = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPath = intent.getStringExtra(MUSIC_PATH);
        startPlayMusic();
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 初始化音乐播放相关界面
     */
    public void initMusicPlayFw() {
        mWm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        mIvMusic = new ImageView(this);
        mIvMusic.setClickable(true);
        mIvMusic.setImageResource(R.drawable.music_playing);
        mIvMusic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //此处使用到了ShapeDrawable
        mProgressShape = new ShapeDrawable();
        mIvMusic.setBackgroundDrawable(mProgressShape);

//        就算设置了也无效，具体原因待查,个人觉得应该是ImageView没有父布局导致的
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DimenUtil.dp2px(this, 100), DimenUtil.dp2px(this, 100));
//        mIvMusic.setLayoutParams(params);

        mIvMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    stopShowProcess();
                    mIvMusic.setImageResource(R.drawable.music_pause);
                } else {
                    mMediaPlayer.start();
                    startShowProcess();
                    mIvMusic.setImageResource(R.drawable.music_playing);
                }
            }
        });

        mMusicViewParams = new WindowManager.LayoutParams();
        mMusicViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mMusicViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mMusicViewParams.format = PixelFormat.RGBA_8888;
        mMusicViewParams.width = DimenUtil.dp2px(this, 40);
        mMusicViewParams.height = DimenUtil.dp2px(this, 40);
        mMusicViewParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        //这里设置的应该偏移量
        mMusicViewParams.x = DimenUtil.dp2px(this, 20);
        mMusicViewParams.y = DimenUtil.dp2px(this, 60);
    }

    /**
     * 显示音乐播放悬浮窗
     */
    public void showFw() {
        if (!isFwShow) {
            mWm.addView(mIvMusic, mMusicViewParams);
            isFwShow = true;
        }
    }

    /**
     * 隐藏音乐播放悬浮窗
     */
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

    /**
     * 开始音乐播放
     */
    private void startPlayMusic() {
        try {
            if (!isEnd && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                stopShowProcess();
            }
            mMediaPlayer = new MediaPlayer();  //必须这样，否则就报IllegalStateException
            mMediaPlayer.setDataSource(mPath);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    showFw();
                    mMediaPlayer.start();
                    startShowProcess();
                    isEnd = false;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    isEnd = true;
                    stopSelf();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始进度条显示
     */
    public void startShowProcess() {
        handler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
        //若不立即发送会出现黑边的
    }

    /**
     * 关闭进度条显示
     */
    public void stopShowProcess() {
        handler.removeMessages(MSG_PROGRESS_UPDATE);
    }

    /**
     * 更新进度条显示
     */
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
            if (sweepAngel <= 360) {
                handler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, PROGRESS_UPDATE_INTERVAL);
            }
        }
    }


    @Override
    public void onDestroy() {
        if (!isEnd && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        hideFw();
        stopShowProcess();
        super.onDestroy();
    }
}
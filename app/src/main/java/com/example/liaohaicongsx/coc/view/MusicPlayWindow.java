package com.example.liaohaicongsx.coc.view;

import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.DimenUtil;

import java.io.IOException;

/**
 * Author Hancher
 * Date 2017/05/19
 * Contact liaohaicongsx@gomo.com
 * <p/>
 * 音乐播放界面
 */
public class MusicPlayWindow {

    public static final int MSG_PROGRESS_UPDATE = 1010;
    public static final int PROGRESS_UPDATE_INTERVAL = 1000;

    public static final int PROGRESS_PADDING = 8;

    /**
     * 最小滑动距离
     */
    public static final int TOUCH_SLOP = 20;

    private Service mCtx;

    private MediaPlayer mMediaPlayer;
    private WindowManager mWm;
    private WindowManager.LayoutParams mMusicViewParams;

    private ImageView mIvMusic;
    private ShapeDrawable mProgressShape;

    private int mXdelta, mYdelta;
    private int mFromX, mFromY;
    private int mToX, mToY;
    private int mDisX, mDisY;
    private long mCurrentTime;

    /**
     * 窗口是否显示的标志
     */
    private boolean mIsFwShow = false;

    //非静态内部类默认持有外部类的引用,内存泄漏隐患？不会！通过逻辑处理
    private Handler mHandler = new Handler() {
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


    public MusicPlayWindow(Service context) {
        mCtx = context;
        mMediaPlayer = new MediaPlayer();
        init();
    }


    /**
     * 初始化音乐播放相关界面
     */
    public void init() {
        mWm = (WindowManager) mCtx.getSystemService(Context.WINDOW_SERVICE);

        mIvMusic = new ImageView(mCtx);
        mIvMusic.setImageResource(R.drawable.music_playing);
        mIvMusic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //此处使用到了ShapeDrawable
        mProgressShape = new ShapeDrawable();
        mIvMusic.setBackgroundDrawable(mProgressShape);

//        就算设置了也无效，具体原因待查,个人觉得应该是ImageView没有父布局导致的
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DimenUtil.dp2px(this, 100), DimenUtil.dp2px(this, 100));
//        mIvMusic.setLayoutParams(params);

        mIvMusic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFromX = (int) event.getRawX();
                        mFromY = (int) event.getRawY();
                        mCurrentTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mToX = (int) event.getRawX();
                        mToY = (int) event.getRawY();
                        mDisX = mToX - mFromX;
                        mDisY = mToY - mFromY;
                        if (mDisX * mDisX + mDisY * mDisY > TOUCH_SLOP * TOUCH_SLOP) {
                            updatePosition(mDisX, mDisY);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mXdelta = mMusicViewParams.x;
                        mYdelta = mMusicViewParams.y;
                        mToX = (int) event.getRawX();
                        mToY = (int) event.getRawY();
                        mDisX = mToX - mFromX;
                        mDisY = mToY - mFromY;
                        if (mDisX * mDisX + mDisY * mDisY < TOUCH_SLOP * TOUCH_SLOP) {
                            if (System.currentTimeMillis() - mCurrentTime < 1000) {
                                if (mMediaPlayer.isPlaying()) {
                                    pauseMusicPlay();
                                } else {
                                    continueMusicPlay();
                                }
                            } else {
                                mCtx.stopSelf();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        mMusicViewParams = new WindowManager.LayoutParams();
        mMusicViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mMusicViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mMusicViewParams.format = PixelFormat.RGBA_8888;
        mMusicViewParams.width = DimenUtil.dp2px(mCtx, 40);
        mMusicViewParams.height = DimenUtil.dp2px(mCtx, 40);
        mMusicViewParams.gravity = Gravity.BOTTOM | Gravity.START;
        //这里设置的应该是偏移量
        mXdelta = DimenUtil.dp2px(mCtx, 20);
        mYdelta = DimenUtil.dp2px(mCtx, 60);
        mMusicViewParams.x = mXdelta;
        mMusicViewParams.y = mYdelta;
    }

    /**
     * 显示音乐播放悬浮窗
     */
    public void show() {
        if (!mIsFwShow) {
            mWm.addView(mIvMusic, mMusicViewParams);
            mIsFwShow = true;
        }
    }

    /**
     * 隐藏音乐播放悬浮窗
     */
    public void hide() {
        if (mIsFwShow) {
            mWm.removeView(mIvMusic);
            mIsFwShow = false;
        }
    }

    /**
     * 更新音乐播放悬浮窗的位置
     */
    public void updatePosition(int disX, int disY) {
        mMusicViewParams.x = mXdelta + disX;
        mMusicViewParams.y = mYdelta - disY;
        mWm.updateViewLayout(mIvMusic, mMusicViewParams);
    }

    /**
     * 重置音乐播放悬浮窗的界面
     */
    public void resetView() {
        mIvMusic.setImageResource(R.drawable.music_playing);
    }

    /**
     * 开始音乐播放
     */
    public void startPlayMusic(String path) {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                //必须调用reset重置下，否则报IllegalStateException
                mMediaPlayer.reset();
                //避免处于暂停状态下，图片显示错误，所以需要reset下
                resetView();
                stopShowProcess();
            }
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    show();
                    mMediaPlayer.start();
                    startShowProcess();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCtx.stopSelf();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐播放
     */
    public void pauseMusicPlay() {
        mMediaPlayer.pause();
        stopShowProcess();
        mIvMusic.setImageResource(R.drawable.music_pause);
    }

    /**
     * 继续音乐播放
     */
    public void continueMusicPlay() {
        mMediaPlayer.start();
        startShowProcess();
        mIvMusic.setImageResource(R.drawable.music_playing);
    }


    /**
     * 开始进度条显示
     */
    public void startShowProcess() {
        mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
        //若不立即发送会出现黑边的
    }

    /**
     * 关闭进度条显示
     */
    public void stopShowProcess() {
        mHandler.removeMessages(MSG_PROGRESS_UPDATE);
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

                    paint.setColor(mCtx.getResources().getColor(R.color.sky_blue));
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeCap(Paint.Cap.SQUARE);
                    paint.setStrokeWidth(16);

                    canvas.drawArc(rect, -90, sweepAngel, false, paint);
                }
            });
            if (sweepAngel <= 360) {
                mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, PROGRESS_UPDATE_INTERVAL);
            }
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        hide();
        stopShowProcess();
    }

}

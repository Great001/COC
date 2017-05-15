package com.example.liaohaicongsx.coc.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * Created by liaohaicongsx on 2017/05/03.
 */
public class NotifyFloatingView extends LinearLayout {

    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;
    private OnNotifyClickListener onNotifyClickListener;

    private TextView tvContent;
    private boolean isAttchToWindow;

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isAttchToWindow()) {
                windowManager.removeView(NotifyFloatingView.this);
            }
        }
    };

    public NotifyFloatingView(Context context) {
        this(context, null);
            }

    public NotifyFloatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotifyFloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handler = new Handler();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        setupFloatingWindow();
        View notifyView = LayoutInflater.from(context).inflate(R.layout.notifyview, null);
        tvContent = (TextView) notifyView.findViewById(R.id.tv_notify_msg);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(notifyView, params);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNotifyClickListener != null) {
                    onNotifyClickListener.onNotifyClick();
                }
                hide();
            }
        });
            }


    //悬浮窗参数设置
    public void setupFloatingWindow() {
        windowParams = new WindowManager.LayoutParams();
        windowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;   //系统级窗口
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;   //不获取焦点
        windowParams.format = PixelFormat.RGBA_8888;   //图像模式
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;  //居上，居左
        windowParams.y = 0;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }


    public void show(IMMessage message) {
        if (!isAttchToWindow()) {
            windowManager.addView(this, windowParams);
            handler.postDelayed(runnable, 5000);
        } else {
//            移除重新计算
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 5000);
        }
        tvContent.setText(Html.fromHtml(message.getContent(), new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = getResources().getDrawable(Integer.valueOf(source));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        }, null));
    }

    public void hide() {
        if (isAttchToWindow()) {
            windowManager.removeView(this);
            handler.removeCallbacks(runnable);
        }
    }


    public boolean isAttchToWindow() {
        return isAttchToWindow;
    }

    public void setAttchToWindow(boolean attchToWindow) {
        isAttchToWindow = attchToWindow;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setAttchToWindow(true);
        Log.d("Notify", "onAttch");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setAttchToWindow(false);
        Log.d("Notify", "onDetach");
    }

    public interface OnNotifyClickListener {
        void onNotifyClick();
    }

    public void setOnNotifyClickListener(OnNotifyClickListener listener) {
        this.onNotifyClickListener = listener;
    }


}

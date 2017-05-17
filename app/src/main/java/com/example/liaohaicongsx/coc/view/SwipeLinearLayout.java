package com.example.liaohaicongsx.coc.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by liaohaicongsx on 2017/05/16.
 */

/**
 * 实现滑动显示删除按钮
 */
public class SwipeLinearLayout extends LinearLayout {

    public static final String TAG = "SwipeLinearLayout";

    public static final int DIRECTION_EXPAND = 0;
    public static final int DIRECTION_SHINK = 1;


    private Scroller mScroller;

    private int preX, preY;
    private int toX, toY;
    private int disX, disY;

    private static final int touchSlop = 100;


    private int mContentWidth;
    private int mOptionWidth;

    private boolean isScroll = false;

    private int mState = -1;

    private OnSwipeListener mOnSwipeListener;

    public SwipeLinearLayout(Context context) {
        this(context, null);
    }

    public SwipeLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
//        mScroller = new Scroller(context);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mOptionWidth = getChildAt(1).getWidth();
        Log.d(TAG, mOptionWidth + "");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                toX = (int) ev.getRawX();
                disX = preX - toX;
                if (Math.abs(disX) > touchSlop) {
                    disallowInterceptTouchEvent(getParent());
                } else {
                    allowInterceptTouchEvent(getParent());
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                toX = (int) ev.getRawX();
                disX = preX - toX;
                if (Math.abs(disX) > touchSlop) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = (int) event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                toX = (int) event.getRawX();
                disX = preX - toX;
                if (disX > touchSlop) {
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipe(this, DIRECTION_EXPAND);
                    }
                } else if (disX < -touchSlop) {
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipe(this, DIRECTION_SHINK);
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    public void autoScroll(int direction) {
        if (direction == DIRECTION_EXPAND && mState != DIRECTION_EXPAND) {
            scrollBy(mOptionWidth, 0);
            mState = DIRECTION_EXPAND;
        } else if (direction == DIRECTION_SHINK && mState == DIRECTION_EXPAND) {
            scrollBy(-mOptionWidth, 0);
            mState = DIRECTION_SHINK;
        }

    }

    public void disallowInterceptTouchEvent(ViewParent parent) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
            disallowInterceptTouchEvent(parent.getParent());
        }
    }

    public void allowInterceptTouchEvent(ViewParent parent) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(false);
            allowInterceptTouchEvent(parent.getParent());
        }
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public interface OnSwipeListener {
        void onSwipe(SwipeLinearLayout swipeLinearLayout, int direction);
    }

    public void setOnSwipeListener(OnSwipeListener listener) {
        mOnSwipeListener = listener;
    }

}

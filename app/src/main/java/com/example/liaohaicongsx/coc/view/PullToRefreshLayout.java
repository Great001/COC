package com.example.liaohaicongsx.coc.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.DimenUtil;


/**
 * Created by Administrator on 2017/4/14.
 */
public class PullToRefreshLayout extends LinearLayout {

    public static final String TAG = "PullToRefreshLayout";

    public static final int STATUS_FINISH_REFRESH = 0;
    public static final int STATUS_PULL_TO_REFRESH = 1;
    public static final int STATUS_RELEASE_TO_REFRESH = 2;
    public static final int STATUS_REFRESHING = 3;

    public static final int MSG_REFRESH_ERROR = 1000;
    public static final int MSG_FINISH_REFRESH = 1002;

    private static final int TOUCH_SLOP = 10;

    private Context mContext;

    private View mHeadView;
    private TextView mTvRefreshStatus;
    private ProgressBar mPbLoading;

    private ListView mListView;

    private int mHeaderHeight;  //headView的高度
    private int maxTopMargin;
    private int minTopMargin;
    private MarginLayoutParams mMarginLayoutParams;

    private boolean mIsFirstLayout = true;

    private int mFromY;
    private int mToY;
    private int mDisY;

    private int mRefreshStatus = STATUS_FINISH_REFRESH;

    private OnRefreshListener mOnRefreshListener;

    public static final int TIME_OUT = 5000;
    //错误处理，主要针对网络错误
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_ERROR:
                    if (mRefreshStatus == STATUS_REFRESHING) {
                        mTvRefreshStatus.setText(R.string.refresh_failed);
                        mPbLoading.setVisibility(GONE);
                        mHandler.sendEmptyMessageDelayed(MSG_FINISH_REFRESH, 2000);
                    }
                    break;
                case MSG_FINISH_REFRESH:
                    refreshComplete();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setOrientation(VERTICAL);

        mHeadView = LayoutInflater.from(context).inflate(R.layout.layout_header_view, null);
        mTvRefreshStatus = (TextView) mHeadView.findViewById(R.id.tv_refresh_status);
        mPbLoading = (ProgressBar) mHeadView.findViewById(R.id.pb_loading);

        addView(mHeadView);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mIsFirstLayout) {
            mHeaderHeight = mHeadView.getHeight();
            maxTopMargin = mHeaderHeight + DimenUtil.dp2px(mContext, 25);
            minTopMargin = -mHeaderHeight;
            mMarginLayoutParams = (MarginLayoutParams) mHeadView.getLayoutParams();
            mMarginLayoutParams.topMargin = -mHeaderHeight;  //隐藏headView
            mListView = (ListView) getChildAt(1);  //获取listView实例
            mIsFirstLayout = false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canPull()) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:   //这里不能拦截，否则会影响listView的滑动
                    mFromY = (int) ev.getRawY();
                    mMarginLayoutParams = (MarginLayoutParams) mHeadView.getLayoutParams();
                    Log.d(TAG, "actino down");
                    break;
                case MotionEvent.ACTION_MOVE:
                    mToY = (int) ev.getRawY();
                    mDisY = mToY - mFromY;
                    if (mDisY > 0) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        if (mRefreshStatus == STATUS_REFRESHING) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mFromY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mToY = (int) event.getRawY();
                mDisY = mToY - mFromY;
                mFromY = mToY;
                if (mDisY < TOUCH_SLOP) {
                    break;
                }
                if (mRefreshStatus != STATUS_REFRESHING) {
                    mMarginLayoutParams.topMargin += mDisY * 0.4;
                    if (mMarginLayoutParams.topMargin < 15) {
                        mTvRefreshStatus.setText(R.string.pull_to_refresh);
                        mRefreshStatus = STATUS_PULL_TO_REFRESH;
                    } else {
                        mTvRefreshStatus.setText(R.string.release_to_refresh);
                        mRefreshStatus = STATUS_RELEASE_TO_REFRESH;
                    }

                    if (mMarginLayoutParams.topMargin >= maxTopMargin) {
                        mMarginLayoutParams.topMargin = maxTopMargin;
                    } else if (mMarginLayoutParams.topMargin <= minTopMargin) {
                        mMarginLayoutParams.topMargin = minTopMargin;
                    }

                    mHeadView.setLayoutParams(mMarginLayoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:
            default:
                if (mRefreshStatus == STATUS_PULL_TO_REFRESH) {
                    mMarginLayoutParams.topMargin = -mHeaderHeight;
                    mHeadView.setLayoutParams(mMarginLayoutParams);
                    mRefreshStatus = STATUS_FINISH_REFRESH;
                } else if (mRefreshStatus == STATUS_RELEASE_TO_REFRESH) {
                    mPbLoading.setVisibility(VISIBLE);
                    mTvRefreshStatus.setText(R.string.refreshing);
                    mRefreshStatus = STATUS_REFRESHING;
//                        mHandler.sendEmptyMessageAtTime(MSG_REFRESH_ERROR, SystemClock.uptimeMillis() + TIME_OUT);
                    doRefresh();
                }
                break;
        }
        return true;
    }

    public boolean canPull() {
        if (mListView.getChildCount() != 0) {
            if (mListView.getFirstVisiblePosition() == 0) {
                int firstViewTop = mListView.getChildAt(0).getTop();
                return firstViewTop == mListView.getTop() && mRefreshStatus != STATUS_REFRESHING;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    public void doRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    public void refreshComplete() {
        mPbLoading.setVisibility(GONE);
        mMarginLayoutParams.topMargin = -mHeaderHeight;
        mHeadView.setLayoutParams(mMarginLayoutParams);
        mRefreshStatus = STATUS_FINISH_REFRESH;
    }

    public void refreshError() {
        mHandler.sendEmptyMessage(MSG_REFRESH_ERROR);
    }



    public int getRefreshStatus() {
        return mRefreshStatus;
    }

    /**
     * 更新回调接口
     *
     * @return
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    public void setOnRefreshLister(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

}
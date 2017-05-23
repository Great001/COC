package com.example.liaohaicongsx.coc.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.DimenUtil;

/**
 * Author Hancher
 * Date 2017/05/18
 * Contact liaohaicongsx@gomo.com
 * <p/>
 * 继承ImageView，利用Matrix实现图片的平移、旋转、缩放的动画效果
 */

public class MatrixAnimateView extends ImageView {

    /**
     * 执行动画变换的Matrix
     */
    private Matrix mMatrix;

    /**
     * ImageView显示的Bitmap
     */
    private Bitmap mBitmap;

    /**
     * 缩放比例
     */
    private float mScaleFactor = 0.1f;

    /**
     * 旋转的角度
     */
    private int mRotate = 0;

    /**
     * 横向和竖向的平移距离
     */
    private int mTranslateValue;

    /**
     * 最大的平移距离
     */
    private int mMaxTranslateValue = 0;

    private OnAnimatorEndListener mListener;

    public MatrixAnimateView(Context context) {
        this(context, null);
    }

    public MatrixAnimateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixAnimateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMatrix = new Matrix();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
        mMaxTranslateValue = (DimenUtil.getScreenWidth(context) - mBitmap.getWidth()) / 2;
        startAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mMatrix, null);
        super.onDraw(canvas);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        matrix.set(mMatrix);
        super.setImageMatrix(matrix);
    }

    /**
     * 开启动画变换
     */
    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.1f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMatrix.reset();
                mScaleFactor = (float) animation.getAnimatedValue();
                mMatrix.setScale(mScaleFactor, mScaleFactor);

                if (mTranslateValue <= mMaxTranslateValue) {
                    mTranslateValue += 10;
                }
                mMatrix.preTranslate(mTranslateValue, mTranslateValue);

                if (mRotate < 360) {
                    mRotate += 5;
                }
                mMatrix.preRotate(mRotate, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                postInvalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimatorEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }

    /**
     * 动画结束回调接口
     */
    public interface OnAnimatorEndListener {
        void onAnimatorEnd();
    }

    public void setOnAnimatorEndListener(OnAnimatorEndListener listener) {
        mListener = listener;
    }


}

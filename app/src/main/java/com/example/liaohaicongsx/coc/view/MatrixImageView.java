package com.example.liaohaicongsx.coc.view;

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
 * Created by liaohaicongsx on 2017/05/18.
 * <p/>
 * 利用Matrix实现图片的动画效果
 */

public class MatrixImageView extends ImageView {

    private Matrix mMatrix;
    private Bitmap mBitmap;
    private float mScaleFactor = 0.1f;
    private int mTranslateValue;
    private int mMaxTranslateValue = 0;
    private int mRotate = 0;

    public MatrixImageView(Context context) {
        this(context, null);
    }

    public MatrixImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatrixImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMatrix = new Matrix();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
        mMaxTranslateValue = (DimenUtil.getScreenWidth(context) - mBitmap.getWidth()) / 2;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.1f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScaleFactor = (float) animation.getAnimatedValue();
                mMatrix.reset();
                mMatrix.setScale(mScaleFactor, mScaleFactor);
                if (mTranslateValue < mMaxTranslateValue) {
                    mTranslateValue += 10;
                }
                mMatrix.preTranslate(mTranslateValue, mTranslateValue);

                if (mRotate != 360) {
                    mRotate += 5;
                }
                mMatrix.preRotate(mRotate, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                postInvalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.start();
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
}

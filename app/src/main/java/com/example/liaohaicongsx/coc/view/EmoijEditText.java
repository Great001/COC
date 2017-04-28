package com.example.liaohaicongsx.coc.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by liaohaicongsx on 2017/04/27.
 */
public class EmoijEditText extends EditText {


    public EmoijEditText(Context context) {
        this(context,null);
    }

    public EmoijEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EmoijEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}

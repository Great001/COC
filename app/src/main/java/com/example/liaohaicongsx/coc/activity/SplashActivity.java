package com.example.liaohaicongsx.coc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.view.MatrixAnimateView;

/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity {

    public static final String TAG = "SplashActivity";

    /**
     * 进入首页按钮
     */
    private Button mBtnEnter;
    /**
     * 展示广告
     */
    private ImageView mIvAd;

    /**
     * 动画
     */
    private MatrixAnimateView mIvAppLogo;

    /**
     * app slogan
     */
    private TextView mTvSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        setContentView(R.layout.activity_splash);

        AppActivityManager.getAppActivityManager().push(this);

        mBtnEnter = (Button) findViewById(R.id.btn_enter_main_page);
        mIvAd = (ImageView) findViewById(R.id.iv_splash);
        mIvAppLogo = (MatrixAnimateView) findViewById(R.id.iv_app_logo);
        mTvSlogan = (TextView) findViewById(R.id.tv_slogan);

        mIvAppLogo.setOnAnimatorEndListener(new MatrixAnimateView.OnAnimatorEndListener() {
            @Override
            public void onAnimatorEnd() {
                startSloganAnimation();
            }
        });

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserModel.getInstance().getLoginState()) {
                    NavigationUtil.navigateToMainPage(SplashActivity.this);
                } else {
                    NavigationUtil.navigateToLoginPage(SplashActivity.this);
                }
                finish();
            }
        });
    }

    /**
     * 开启app slogan 的动画显示
     */
    public void startSloganAnimation() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mTvSlogan.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mBtnEnter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mTvSlogan.startAnimation(alphaAnimation);
    }

}

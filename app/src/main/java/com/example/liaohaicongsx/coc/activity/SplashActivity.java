package com.example.liaohaicongsx.coc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.view.MatrixImageView;

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

    private MatrixImageView mIvAppLogo;

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
        mIvAppLogo = (MatrixImageView) findViewById(R.id.iv_app_logo);

//        MyImageLoader.getInstance(getApplicationContext()).displayImage("http://i2.muimg.com/1949/b490c083259a5dca.jpg",mIvAd);

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

        startAnimation();
    }

    public void startAnimation() {

    }

}

package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;

public class SplashActivity extends AppCompatActivity {

    private Button mBtnEnter;   //进入首页按钮
    private ImageView mIvAd;  //展示广告

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppActivityManager.getInstance().push(this);

        mBtnEnter = (Button) findViewById(R.id.btn_enter_main_page);
        mIvAd = (ImageView) findViewById(R.id.iv_splash);

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserModel.getInstance().getLoginState()) {
                    NavigationUtil.navigatoMainPage(SplashActivity.this);
                } else {
                    NavigationUtil.navigateToLoginPage(SplashActivity.this);
                }
                finish();
            }
        });
    }
}

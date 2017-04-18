package com.example.liaohaicongsx.coc.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.CocApplication;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.NavigationUtil;

public class SplashActivity extends AppCompatActivity {

    private Button mBtnEnter;
    private ImageView mIvAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBtnEnter = (Button) findViewById(R.id.btn_enter_main_page);
        mIvAd = (ImageView) findViewById(R.id.iv_splash);

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLoginState()) {
                    NavigationUtil.navigatoMainPage(SplashActivity.this);
                }else{
                    NavigationUtil.navigateToLoginPage(SplashActivity.this);
                }
                finish();
            }
        });
    }

    public boolean checkLoginState(){
        SharedPreferences sp = getSharedPreferences(CocApplication.SP_USER,MODE_PRIVATE);
        return sp.getBoolean("loginState",false);
    }

}

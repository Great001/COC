package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;

import com.example.liaohaicongsx.coc.R;

/**
 * 找回密码页面
 */
public class FindPwdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.find_pwd_page);
    }
}

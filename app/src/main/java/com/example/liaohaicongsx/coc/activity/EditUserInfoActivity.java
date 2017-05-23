package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;

import com.example.liaohaicongsx.coc.R;

/**
 * 编辑用户资料页面
 */
public class EditUserInfoActivity extends BaseActivity {

    public static final String EDIT_ITEM = "edit_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.edit_user_info);
    }
}

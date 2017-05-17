package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.UserInfoAdapter;
import com.example.liaohaicongsx.coc.view.SwipeLinearLayout;
/**
 * Author Hancher
 * Date 2017/05/17
 * Contact liaohaicongsx@gomo.com
 */

/**
 * 用户个人详情页面
 */
public class UserInfoActivity extends BaseActivity implements SwipeLinearLayout.OnSwipeListener {

    private ListView mLvUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mLvUserInfo = (ListView) findViewById(R.id.lv_user_info);
        UserInfoAdapter adapter = new UserInfoAdapter(this);
        mLvUserInfo.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int childCount = mLvUserInfo.getChildCount();
            for (int i = 0; i < childCount; i++) {
                SwipeLinearLayout swipeLinearLayout = (SwipeLinearLayout) mLvUserInfo.getChildAt(i);
                swipeLinearLayout.setOnSwipeListener(this);
                swipeLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.user_profile_center);
    }


    @Override
    public void onSwipe(SwipeLinearLayout swipeLinearLayout, int direction) {
        int childCount = mLvUserInfo.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SwipeLinearLayout view = (SwipeLinearLayout) mLvUserInfo.getChildAt(i);
            if (view == swipeLinearLayout) {
                swipeLinearLayout.autoScroll(direction);
            } else {
                if (direction == SwipeLinearLayout.DIRECTION_EXPAND) {
                    view.autoScroll(SwipeLinearLayout.DIRECTION_SHINK);
                }
            }
        }

    }
}

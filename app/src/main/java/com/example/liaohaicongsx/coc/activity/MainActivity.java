package com.example.liaohaicongsx.coc.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.api.RetrofitClient;
import com.example.liaohaicongsx.coc.fragment.ContactsFragment;
import com.example.liaohaicongsx.coc.fragment.DynamicFragment;
import com.example.liaohaicongsx.coc.fragment.MessageFragment;
import com.example.liaohaicongsx.coc.model.ResponseUser;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.util.ToastUtil;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDlMainPage;
    private ViewPager mVpTabs;
    private RadioGroup mRgSelectTab;
    private TextView mTvNickName;
    private TextView mTvSetting;

    private RelativeLayout mRlUserCard;

    private int mCurrentItem;

    private long mExitTime;

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new MessageFragment();
                    break;
                case 1:
                    fragment = new ContactsFragment();
                    break;
                case 2:
                    fragment = new DynamicFragment();
                    break;
                default:
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.avatar);
            actionBar.setTitle("");
        }
        AppActivityManager.getAppActivityManager().push(this);
        initView();

        //弹窗权限请求,但好像无效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 111);
            }
        }
        Toast.makeText(this, "请先到设置界面开通悬浮窗和锁屏显示权限", Toast.LENGTH_LONG).show();
    }

    public void initView() {
        mDlMainPage = (DrawerLayout) findViewById(R.id.dl_main_page);
        mRgSelectTab = (RadioGroup) findViewById(R.id.rg_tabs_select);
        mVpTabs = (ViewPager) findViewById(R.id.vp_main_tabs);
//        mLvUserProfile = (ListView) findViewById(R.id.lv_user_profile);
        mTvSetting = (TextView) findViewById(R.id.tv_setting);
        mTvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigateToSettingPage(MainActivity.this);
            }
        });
        mRlUserCard = (RelativeLayout) findViewById(R.id.rl_user_card);
        mRlUserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigateToUserInfoPage(MainActivity.this);
                mDlMainPage.closeDrawer(Gravity.LEFT);
            }
        });

        mTvNickName = (TextView) findViewById(R.id.tv_user_nickname);
        mTvNickName.setText(UserModel.getInstance().getUserInfo().getName());

        mVpTabs.setAdapter(mFragmentPagerAdapter);
        mCurrentItem = 0;
        mVpTabs.setCurrentItem(mCurrentItem);
        ((RadioButton) mRgSelectTab.getChildAt(mCurrentItem)).setTextColor(getResources().getColor(R.color.colorPrimary));
        mVpTabs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int checkId = R.id.rbtn_message;
                switch (position) {
                    case 0:
                        checkId = R.id.rbtn_message;
                        break;
                    case 1:
                        checkId = R.id.rbtn_contacts;
                        break;
                    case 2:
                        checkId = R.id.rbtn_dynamic;
                        break;
                    default:
                        break;
                }
                mRgSelectTab.check(checkId);

                ((RadioButton) mRgSelectTab.getChildAt(mCurrentItem)).setTextColor(getResources().getColor(R.color.black));
                ((RadioButton) mRgSelectTab.getChildAt(position)).setTextColor(getResources().getColor(R.color.colorPrimary));
                mCurrentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRgSelectTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_message:
                        mVpTabs.setCurrentItem(0);
                        break;
                    case R.id.rbtn_contacts:
                        mVpTabs.setCurrentItem(1);
                        break;
                    case R.id.rbtn_dynamic:
                        mVpTabs.setCurrentItem(2);
                        break;
                    default:
                        break;
                }

            }
        });
        RetrofitClient.getInstance().userRegister("six", "")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseUser>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d("Main", "error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseUser responseUser) {
                        Log.d("Main", responseUser.toString());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDlMainPage.openDrawer(Gravity.LEFT);
                break;
            case R.id.item_add_friend:
                NavigationUtil.navigateToAddFriendActivity(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 检查是否还有fragment退栈
     *
     * @return
     */
    public boolean canPopBack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
            return true;
        }
        return false;
    }



    @Override
    public void onBackPressed() {
        if (!canPopBack()) {
            if (System.currentTimeMillis() - mExitTime <= 2000) {
                AppActivityManager.getAppActivityManager().clear();
                System.exit(1);
            } else {
                mExitTime = System.currentTimeMillis();
                ToastUtil.show(this, R.string.click_once_more_to_exit);
            }
        }

    }

    /**
     * 获取当前Activity所在哪个tab中
     *
     * @return
     */
    public int getCurrentItem() {
        return mCurrentItem;
    }

}


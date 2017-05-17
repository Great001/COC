package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.api.RetrofitClient;
import com.example.liaohaicongsx.coc.fragment.ContactsFragment;
import com.example.liaohaicongsx.coc.fragment.DynamicFragment;
import com.example.liaohaicongsx.coc.fragment.MessageFragment;
import com.example.liaohaicongsx.coc.model.ResponseUser;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;

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

    private ListView mLvUserProfile;
    private RelativeLayout mRlUserCard;

    private int mCurrentItem;

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
    }

    public void initView() {
        mDlMainPage = (DrawerLayout) findViewById(R.id.dl_main_page);
        mRgSelectTab = (RadioGroup) findViewById(R.id.rg_tabs_select);
        mVpTabs = (ViewPager) findViewById(R.id.vp_main_tabs);
        mLvUserProfile = (ListView) findViewById(R.id.lv_user_profile);
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


    FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
}


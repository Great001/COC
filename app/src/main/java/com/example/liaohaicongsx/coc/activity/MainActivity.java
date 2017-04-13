package com.example.liaohaicongsx.coc.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.fragment.ContactsFragment;
import com.example.liaohaicongsx.coc.fragment.DynamicFragment;
import com.example.liaohaicongsx.coc.fragment.MessageFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDlMainpage;
    private ViewPager mVpTabs;
    private RadioGroup mRgSelectTab;

    private int currentItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        mDlMainpage = (DrawerLayout) findViewById(R.id.dl_main_page);
        mRgSelectTab = (RadioGroup) findViewById(R.id.rg_tabs_select);
//        mVpTabs = (ViewPager) findViewById(R.id.vp_main_tabs);

        mVpTabs.setAdapter(tabsAdapter);
        currentItem = 0;
        mVpTabs.setCurrentItem(currentItem);
        ((RadioButton)mRgSelectTab.getChildAt(currentItem)).setTextColor(getResources().getColor(R.color.colorPrimary));
        mVpTabs.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ((RadioButton)mRgSelectTab.getChildAt(currentItem)).setTextColor(getResources().getColor(R.color.black));
                ((RadioButton)mRgSelectTab.getChildAt(position)).setTextColor(getResources().getColor(R.color.colorPrimary));
                currentItem = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRgSelectTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
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

    }


    FragmentPagerAdapter tabsAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
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

}

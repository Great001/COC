package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.NewFriendAdapter;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友认证页面
 */
public class AddFriendVefifyActivity extends BaseActivity {

    private ListView mLvNewFriends;
    private NewFriendAdapter mAdapter;
    private List<SystemMessage> mSystemMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_vefify);

        SystemMessage message = (SystemMessage) getIntent().getSerializableExtra("message");
        mSystemMessages.add(message);


        mLvNewFriends = (ListView) findViewById(R.id.lv_new_friends);
        mAdapter = new NewFriendAdapter(this);
        mLvNewFriends.setAdapter(mAdapter);
        mAdapter.setData(mSystemMessages);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.add_friend_verify_page);
    }
}

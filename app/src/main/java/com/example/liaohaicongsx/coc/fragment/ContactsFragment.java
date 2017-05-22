package com.example.liaohaicongsx.coc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.ContactsAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人页面
 */
public class ContactsFragment extends Fragment {

    private ListView mLvContacts;
    private ContactsAdapter mAdapter;
    private List<NimUserInfo> mUserInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contacts, container, false);
        mLvContacts = (ListView) view.findViewById(R.id.lv_contacts);

        // 获取所有好友帐号
        List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts();
        // 获取所有好友用户资料
        mUserInfoList = NIMClient.getService(UserService.class).getUserInfoList(accounts);
        mAdapter = new ContactsAdapter(getContext());
        if (mUserInfoList != null) {
            mAdapter.setUserInfoList(mUserInfoList);
        }
        mLvContacts.setAdapter(mAdapter);
        return view;
    }
}

package com.example.liaohaicongsx.coc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.RecentContactAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {

    private ListView mLvRecentContact;
    private RecentContactAdapter mAdapter;

    private List<RecentContact> mListRecentContact;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_message, null);
        mLvRecentContact = (ListView) view.findViewById(R.id.lv_recent_contact);
        mAdapter = new RecentContactAdapter(getContext());
        mLvRecentContact.setAdapter(mAdapter);

        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, List<RecentContact> result, Throwable exception) {
                if (result != null) {
                    mListRecentContact = result;
                    mAdapter.setContacts(mListRecentContact);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }
}

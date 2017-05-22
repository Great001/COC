package com.example.liaohaicongsx.coc.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.RecentContactAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * 最近消息页
 */
public class MessageFragment extends Fragment {

    private ListView mLvRecentContact;
    private RecentContactAdapter mAdapter;

    private List<RecentContact> mListRecentContact;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateRecentContact();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_message, container, false);
        mLvRecentContact = (ListView) view.findViewById(R.id.lv_recent_contact);
        mAdapter = new RecentContactAdapter(getContext());
        mLvRecentContact.setAdapter(mAdapter);

        //注册广播
        getActivity().registerReceiver(mReceiver, new IntentFilter("com.example.liaohaicongsx.coc.RecentContact.update"));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRecentContact();
    }

    /**
     * 请求更新最近联系人列表
     */
    public void updateRecentContact() {
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
            @Override
            public void onResult(int code, List<RecentContact> result, Throwable exception) {
                if (code == 200 && result != null) {
                    mListRecentContact = result;
                    mAdapter.setRecentContacts(mListRecentContact);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(mReceiver);
    }
}

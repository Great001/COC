package com.example.liaohaicongsx.coc.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.LvAdapter;
import com.example.liaohaicongsx.coc.view.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态页面
 */
public class DynamicFragment extends Fragment implements PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout mPrflTest;
    private ListView mLvTest;
    private LvAdapter mAdapter;
    private List<String> mListImgUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dynamic, null);
        mPrflTest = (PullToRefreshLayout) view.findViewById(R.id.prfl_test);
        mLvTest = (ListView) view.findViewById(R.id.lv_test);
        mPrflTest.setOnRefreshLister(this);
        mListImgUrl = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mListImgUrl.add("http://i2.muimg.com/1949/732523acc12d5e27.jpg");
            mListImgUrl.add("http://i2.muimg.com/1949/170b75304ea82905.jpg");
            mListImgUrl.add("http://i2.muimg.com/1949/3aff8f1b5523beb7.jpg");
//            mListImgUrl.add("http://i2.muimg.com/1949/0f5cee8f7dd92b41.jpg");
//            mListImgUrl.add("http://i2.muimg.com/1949/5996c150cb12db75.jpg");
        }

        mAdapter = new LvAdapter(getActivity(), mListImgUrl);
        mLvTest.setAdapter(mAdapter);


        mLvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageFragment.newInstance(mListImgUrl.get(position)).show(getActivity().getFragmentManager(), mListImgUrl.get(position));
            }
        });
        return view;
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPrflTest.refreshComplete();
            }
        }, 1500);


    }
}

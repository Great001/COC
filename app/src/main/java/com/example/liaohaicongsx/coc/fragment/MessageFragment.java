package com.example.liaohaicongsx.coc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;


public class MessageFragment extends Fragment {

    private TextView tvResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_message,null);
        tvResult = (TextView) view.findViewById(R.id.tv_result);
        return view;
    }

    public void setResult(String str){
        tvResult.setText(str);
    }

}

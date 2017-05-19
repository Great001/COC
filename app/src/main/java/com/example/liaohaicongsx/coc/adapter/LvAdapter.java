package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.MyImageLoader.MyImageLoader;
import com.example.liaohaicongsx.coc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/05/05.
 */
public class LvAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mListImgUrl = new ArrayList<>();

    public LvAdapter(Context context, List<String> list) {
        this.mContext = context;
        mListImgUrl = list;
    }


    @Override
    public int getCount() {
        return mListImgUrl.size();
    }

    @Override
    public Object getItem(int position) {
        return mListImgUrl.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.mTvNum.setText(position + "");
        MyImageLoader.getInstance(mContext.getApplicationContext()).displayImage(mListImgUrl.get(position), holder.mIvTest);
        return convertView;
    }

    /**
     * 图片item
     */
    class MyViewHolder {
        TextView mTvNum;
        ImageView mIvTest;

        MyViewHolder(View itemView) {
            mTvNum = (TextView) itemView.findViewById(R.id.tv_test);
            mIvTest = (ImageView) itemView.findViewById(R.id.iv_test);
        }

    }

}

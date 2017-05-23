package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.view.SwipeLinearLayout;

/**
 * Created by liaohaicongsx on 2017/05/17.
 */
public class UserInfoAdapter extends BaseAdapter {

    private String mItemNames[] = {"性别：", "年龄:", "职业：", "爱好："};
    private String mItemContent[] = {"男", "22", "IT", "广泛"};

    private Context mCtx;

    public UserInfoAdapter(Context context) {
        mCtx = context;
    }

    @Override
    public int getCount() {
        return mItemNames.length;
    }

    @Override
    public Object getItem(int position) {
        return mItemNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.lv_user_info_item, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.mTvName.setText(mItemNames[position]);
        holder.mTvContent.setText(mItemContent[position]);
        holder.mTvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigateToEditUserInfoPage(mCtx, mItemNames[position]);
                holder.mView.autoScroll(SwipeLinearLayout.DIRECTION_SHINK);
            }
        });
        return convertView;
    }

    /**
     * 用户个人信息item
     */
    private class MyViewHolder {
        TextView mTvName;
        TextView mTvContent;
        TextView mTvEdit;

        SwipeLinearLayout mView;

        MyViewHolder(View itemView) {
            mView = (SwipeLinearLayout) itemView;
            mTvName = (TextView) itemView.findViewById(R.id.tv_item_name);
            mTvContent = (TextView) itemView.findViewById(R.id.tv_item_content);
            mTvEdit = (TextView) itemView.findViewById(R.id.tv_user_info_edit);
        }
    }

}

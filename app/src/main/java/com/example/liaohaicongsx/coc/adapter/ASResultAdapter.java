package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.ResponseUserInfos;
import com.example.liaohaicongsx.coc.model.UserInfo;
import com.example.liaohaicongsx.coc.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/19.
 */
public class ASResultAdapter extends BaseAdapter {

    private Context context;

    private List<UserInfo> userInfos = new ArrayList<>();

    public ASResultAdapter(Context context) {
        this.context = context;
    }

    public void setSearchData(List<UserInfo> uinfos) {
        userInfos = uinfos;
    }


    @Override
    public int getCount() {
        return userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.accout_search_lv_item,null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);

        }else{
            holder = (MyViewHolder) convertView.getTag();
        }
        UserInfo userInfo = userInfos.get(position);
        holder.tvName.setText(userInfo.getName());
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(context,"添加成功");
            }
        });
        return convertView;
    }

    class MyViewHolder{
        ImageView ivAvatar;
        TextView tvName;
        TextView tvAdd;

        public MyViewHolder(View itemView){
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_item_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvAdd = (TextView) itemView.findViewById(R.id.tv_item_add_friend);
        }


    }

}

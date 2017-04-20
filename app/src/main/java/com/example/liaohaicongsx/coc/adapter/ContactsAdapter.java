package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/20.
 */
public class ContactsAdapter extends BaseAdapter {

    private Context context;
    private List<NimUserInfo> userInfos = new ArrayList<>();

    public ContactsAdapter(Context context,List<NimUserInfo> list){
        this.context = context;
//        userInfos = list;
    }


    public ContactsAdapter(Context context){
        this.context = context;
    }

    public void setUserInfos(List<NimUserInfo> list){
        userInfos = list;
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
        ContactViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_contacts_item,null);
            holder = new ContactViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ContactViewHolder) convertView.getTag();
        }
        final NimUserInfo user = userInfos.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigateToChatPage(context,user.getAccount());
            }
        });
        return convertView;
    }

    class ContactViewHolder{
        ImageView ivAvatar;
        TextView tvName;
        TextView tvSign;

        public ContactViewHolder(View itemView){
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_contacts_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tv_contacts_name);
            tvSign = (TextView) itemView.findViewById(R.id.tv_contacts_signature);
        }
    }

}

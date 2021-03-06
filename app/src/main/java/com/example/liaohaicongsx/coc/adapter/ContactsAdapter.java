package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.text.TextUtils;
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

    private Context mContext;
    private List<NimUserInfo> mUserInfoList = new ArrayList<>();

    public ContactsAdapter(Context context, List<NimUserInfo> list) {
        this.mContext = context;
        mUserInfoList = list;
    }


    public ContactsAdapter(Context context) {
        this.mContext = context;
    }

    public void setUserInfoList(List<NimUserInfo> list) {
        mUserInfoList = list;
    }

    @Override
    public int getCount() {
        return mUserInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_contacts_item, parent, false);
            holder = new ContactViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ContactViewHolder) convertView.getTag();
        }
        final NimUserInfo user = mUserInfoList.get(position);
        final String account = user.getAccount();
        final String name = user.getName();
        String signature = user.getSignature();

        holder.mTvName.setText(TextUtils.isEmpty(name) ? "chatter" : name);
        holder.mTvSign.setText(TextUtils.isEmpty(signature) ? "海内存知己，天涯若比邻" : signature);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtil.navigateToChatPage(mContext, account, name);
            }
        });
        return convertView;
    }

    /**
     * 联系人item
     */
    class ContactViewHolder {
        ImageView mIvAvatar;
        TextView mTvName;
        TextView mTvSign;

        public ContactViewHolder(View itemView) {
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_contacts_avatar);
            mTvName = (TextView) itemView.findViewById(R.id.tv_contacts_name);
            mTvSign = (TextView) itemView.findViewById(R.id.tv_contacts_signature);
        }
    }

}

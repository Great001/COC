package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/21.
 */
public class RecentContactAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecentContact> mListRecentContact = new ArrayList<>();

    public RecentContactAdapter(Context context) {
        this.mContext = context;
    }

    public void setContacts(List<RecentContact> list) {
        mListRecentContact = list;
    }

    @Override
    public int getCount() {
        return mListRecentContact.size();
    }

    @Override
    public Object getItem(int position) {
        return mListRecentContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecentContactViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_recent_contact_item, null);
            holder = new RecentContactViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RecentContactViewHolder) convertView.getTag();
        }
        final RecentContact recentContact = mListRecentContact.get(position);
        final String name = TextUtils.isEmpty(recentContact.getFromNick()) ? "test" : recentContact.getFromNick();
        final String account = recentContact.getFromAccount();
        String msg = recentContact.getContent();
        int unReadCount = recentContact.getUnreadCount();
        Date date = new Date(recentContact.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String time = simpleDateFormat.format(date);

        holder.mTvName.setText(name);
        holder.mTvMsg.setText(Html.fromHtml(msg, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = mContext.getResources().getDrawable(Integer.valueOf(source));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        }, null));
        holder.mTvTime.setText(time);

        if (unReadCount > 0) {
            holder.mTvUnreadCount.setVisibility(View.VISIBLE);
            if (unReadCount < 100) {
                holder.mTvUnreadCount.setText(String.valueOf(unReadCount));
            } else {
                holder.mTvUnreadCount.setText("99+");
            }
        } else {
            holder.mTvUnreadCount.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.findViewById(R.id.tv_recent_unread_count).setVisibility(View.GONE);
                NavigationUtil.navigateToChatPage(mContext, account, name);
            }
        });
        return convertView;
    }

    /**
     * 最近联系人item
     */
    class RecentContactViewHolder {
        ImageView mIvAvatar;
        TextView mTvName;
        TextView mTvMsg;
        TextView mTvTime;
        TextView mTvUnreadCount;

        RecentContactViewHolder(View itemView) {
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_recent_contact_avatar);
            mTvName = (TextView) itemView.findViewById(R.id.tv_recent_contact_name);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_recent_contact_msg);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_recent_contact_time);
            mTvUnreadCount = (TextView) itemView.findViewById(R.id.tv_recent_unread_count);
        }
    }

}

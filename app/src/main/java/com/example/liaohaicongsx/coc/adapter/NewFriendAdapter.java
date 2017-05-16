package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/20.
 */
public class NewFriendAdapter extends BaseAdapter {

    private Context mContext;
    private List<SystemMessage> mSystemMessageList = new ArrayList<>();

    public NewFriendAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<SystemMessage> list) {
        mSystemMessageList = list;
    }


    @Override
    public int getCount() {
        return mSystemMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSystemMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_new_friend_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SystemMessage message = mSystemMessageList.get(position);
        holder.mTvName.setText(message.getFromAccount());
        holder.mTvHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过验证请求
                NIMClient.getService(FriendService.class).ackAddFriendRequest(message.getFromAccount(), true);
                v.setBackgroundResource(R.color.white);
                v.setClickable(false);
                ((TextView) v).setText("已同意");
            }
        });
        return convertView;
    }


    /**
     *
     */
    class ViewHolder {
        ImageView mIvAvatar;
        TextView mTvName;
        TextView mTvRequest;
        TextView mTvHandler;

        ViewHolder(View itemView) {
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_new_friend_avatar);
            mTvHandler = (TextView) itemView.findViewById(R.id.tv_new_friend_handle_request);
            mTvName = (TextView) itemView.findViewById(R.id.tv_new_friend_name);
            mTvRequest = (TextView) itemView.findViewById(R.id.tv_new_friend_request);
        }

    }

}

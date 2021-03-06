package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserInfo;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.ToastUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.constant.VerifyType;
import com.netease.nimlib.sdk.friend.model.AddFriendData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/19.
 */
public class ASResultAdapter extends BaseAdapter {

    private Context mContext;

    private List<UserInfo> mInfoList = new ArrayList<>();

    public ASResultAdapter(Context context) {
        this.mContext = context;
    }

    public void setSearchData(List<UserInfo> uinfos) {
        mInfoList = uinfos;
    }


    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.accout_search_lv_item, null);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        final UserInfo userInfo = mInfoList.get(position);
        holder.mTvName.setText(userInfo.getName());
        holder.mTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final VerifyType verifyType = VerifyType.VERIFY_REQUEST; // 发起好友验证请求
                String myName = UserModel.getInstance().getUserInfo().getName();
                String msg = String.format("好友请求附言：我是%s,我想加你为好友。", myName);
                NIMClient.getService(FriendService.class).addFriend(new AddFriendData(userInfo.getAccid(), verifyType, msg))
                        .setCallback(new RequestCallback<Void>() {
                            @Override
                            public void onSuccess(Void param) {
                                ToastUtil.show(mContext, "添加成功");
                            }

                            @Override
                            public void onFailed(int code) {

                            }

                            @Override
                            public void onException(Throwable exception) {

                            }
                        });
            }
        });
        return convertView;
    }

    /**
     *
     */
    class MyViewHolder {
        ImageView mIvAvatar;
        TextView mTvName;
        TextView mTvAdd;

        public MyViewHolder(View itemView) {
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_item_avatar);
            mTvName = (TextView) itemView.findViewById(R.id.tv_item_name);
            mTvAdd = (TextView) itemView.findViewById(R.id.tv_item_add_friend);
        }
    }
}

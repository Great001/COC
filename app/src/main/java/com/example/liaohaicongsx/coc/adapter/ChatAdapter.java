package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/20.
 */
public class ChatAdapter extends BaseAdapter {

    public static final int ITEM_TYPE_IN = 0;
    public static final int ITEM_TYPE_OUT = 1;

    private Context context;
    private List<IMMessage> messages = new ArrayList<>();

    public ChatAdapter(Context context){
        this.context = context;
    }

    public void setMessages(List<IMMessage> list){
        messages = list;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatViewHolder holder = null;
        if(convertView == null){
            if(getItemViewType(position) == ITEM_TYPE_IN){
                convertView = LayoutInflater.from(context).inflate(R.layout.lv_chat_item_in,null);
            }else{
                convertView = LayoutInflater.from(context).inflate(R.layout.lv_chat_item_out,null);
            }
            holder = new ChatViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ChatViewHolder) convertView.getTag();
        }
        IMMessage message = messages.get(position);
        holder.tvMsg.setText(message.getContent());
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getDirect() == MsgDirectionEnum.In){
            return ITEM_TYPE_IN;
        }else{
            return ITEM_TYPE_OUT;
        }
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    class ChatViewHolder{
        TextView tvMsg;
        ImageView ivAvatar;

        ChatViewHolder(View itemView){
            tvMsg = (TextView) itemView.findViewById(R.id.tv_chat_message);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_chatter_avatar);
        }
    }

}

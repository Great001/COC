package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.service.MusicPlayService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
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
    private Html.ImageGetter imageGetter;

    public ChatAdapter(final Context context) {
        this.context = context;
        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = context.getResources().getDrawable(Integer.valueOf(source));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        };
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
        final IMMessage message = messages.get(position);
        if (message.getMsgType() == MsgTypeEnum.file) {
            holder.tvMsgContent.setVisibility(View.GONE);
            holder.tvMsgFile.setVisibility(View.VISIBLE);
            holder.tvMsgFile.setText(((FileAttachment) message.getAttachment()).getDisplayName());
            holder.tvMsgFile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FileAttachment file = (FileAttachment) message.getAttachment();
                    String path = file.getPath();
                    if (path != null) {
                        Log.d("Chat", path);
                        Intent intent = new Intent(context, MusicPlayService.class);
                        intent.putExtra(MusicPlayService.MUSIC_PATH, file.getPath());
                        context.startService(intent);
                    }
                    return false;
                }
            });
        } else {
            if (holder.tvMsgContent.getVisibility() != View.VISIBLE) {
                holder.tvMsgContent.setVisibility(View.VISIBLE);
                holder.tvMsgFile.setVisibility(View.GONE);
            }
            holder.tvMsgContent.setText(Html.fromHtml(message.getContent(), imageGetter, null));
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage message = messages.get(position);
        if (message.getDirect() == MsgDirectionEnum.In) {
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
        TextView tvMsgContent;
        TextView tvMsgFile;
        ImageView ivAvatar;

        ChatViewHolder(View itemView){
            tvMsgContent = (TextView) itemView.findViewById(R.id.tv_chat_message_content);
            tvMsgFile = (TextView) itemView.findViewById(R.id.tv_chat_message_attachment_music);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_chatter_avatar);
        }
    }

}

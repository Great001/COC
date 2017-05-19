package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.MyImageLoader.ImageResizer;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.service.MusicPlayService;
import com.example.liaohaicongsx.coc.util.DimenUtil;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/20.
 */
public class ChatAdapter extends BaseAdapter {

    public static final int ITEM_TYPE_IN = 0;
    public static final int ITEM_TYPE_OUT = 1;


    private Context mContext;
    private List<IMMessage> mIMMessages = new ArrayList<>();
    private Html.ImageGetter mImageGetter;

    public ChatAdapter(final Context context) {
        this.mContext = context;
        mImageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable drawable = context.getResources().getDrawable(Integer.valueOf(source));
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        };
    }

    public void setIMMessages(List<IMMessage> list) {
        mIMMessages = list;
    }

    @Override
    public int getCount() {
        return mIMMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return mIMMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatViewHolder holder = null;
        if (convertView == null) {
            if (getItemViewType(position) == ITEM_TYPE_IN) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_chat_item_in, null);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_chat_item_out, null);
            }
            holder = new ChatViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChatViewHolder) convertView.getTag();
        }
        final IMMessage message = mIMMessages.get(position);
        if (message.getMsgType() == MsgTypeEnum.file) {
            holder.mTvContent.setVisibility(View.GONE);
            holder.mIvPic.setVisibility(View.GONE);
            holder.mTvMusic.setVisibility(View.VISIBLE);
            holder.mTvMusic.setText(((FileAttachment) message.getAttachment()).getDisplayName());
            holder.mTvMusic.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FileAttachment file = (FileAttachment) message.getAttachment();
                    String path = file.getPath();
                    if (path != null) {
                        Log.d("Chat", path);
                        Intent intent = new Intent(mContext, MusicPlayService.class);
                        intent.putExtra(MusicPlayService.MUSIC_PATH, file.getPath());
                        mContext.startService(intent);
                    }
                    return false;
                }
            });
        } else if (message.getMsgType() == MsgTypeEnum.image) {
            holder.mIvPic.setVisibility(View.VISIBLE);
            holder.mTvContent.setVisibility(View.GONE);
            holder.mTvMusic.setVisibility(View.GONE);

            try {
                FileInputStream fis = new FileInputStream(((ImageAttachment) message.getAttachment()).getPath());
                Bitmap bitmap = ImageResizer.getInstance().decodeSampledBitmapFromFD(fis.getFD(), DimenUtil.dp2px(mContext, 100), DimenUtil.dp2px(mContext, 100));
                holder.mIvPic.setImageBitmap(bitmap);
                holder.mIvPic.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            if (holder.mTvContent.getVisibility() != View.VISIBLE) {
                holder.mTvContent.setVisibility(View.VISIBLE);
                holder.mTvMusic.setVisibility(View.GONE);
                holder.mIvPic.setVisibility(View.GONE);
            }
            holder.mTvContent.setText(Html.fromHtml(message.getContent(), mImageGetter, null));
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage message = mIMMessages.get(position);
        if (message.getDirect() == MsgDirectionEnum.In) {
            return ITEM_TYPE_IN;
        } else {
            return ITEM_TYPE_OUT;
        }
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     *
     */
    class ChatViewHolder {
        TextView mTvContent;
        TextView mTvMusic;
        ImageView mIvAvatar;
        ImageView mIvPic;

        ChatViewHolder(View itemView) {
            mTvContent = (TextView) itemView.findViewById(R.id.tv_chat_message_content);
            mTvMusic = (TextView) itemView.findViewById(R.id.tv_chat_message_attachment_music);
            mIvAvatar = (ImageView) itemView.findViewById(R.id.iv_chatter_avatar);
            mIvPic = (ImageView) itemView.findViewById(R.id.iv_chat_message_image);
        }
    }

}

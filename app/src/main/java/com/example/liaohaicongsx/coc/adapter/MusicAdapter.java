package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;

/**
 * Created by liaohaicongsx on 2017/05/08.
 */

/**
 * 本地音乐列表适配器
 */
public class MusicAdapter extends BaseAdapter {

    private Context mCtx;
    private Cursor mCursor;

    public MusicAdapter(Context context) {
        this.mCtx = context;
    }

    public void setData(Cursor cursor) {
        mCursor = cursor;
        mCursor.moveToFirst();
    }


    @Override
    public int getCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        if (mCursor == null) {
            return null;
        }
        return mCursor.moveToPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MusicViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.lv_music_item, null);
            holder = new MusicViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MusicViewHolder) convertView.getTag();
        }
        mCursor.moveToPosition(position);
        if (!mCursor.isAfterLast()) {
            holder.mTvArtist.setText(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            holder.mTvName.setText(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
        }
        return convertView;
    }

    /**
     * 音乐item
     */
    class MusicViewHolder {
        TextView mTvName;
        TextView mTvArtist;

        MusicViewHolder(View itemView) {
            mTvName = (TextView) itemView.findViewById(R.id.tv_music_name);
            mTvArtist = (TextView) itemView.findViewById(R.id.tv_music_artist);
        }

    }

}

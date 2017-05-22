package com.example.liaohaicongsx.coc.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.MyImageLoader.ImageResizer;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.util.DimenUtil;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by liaohaicongsx on 2017/05/19.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mCtx;
    private Cursor mCursor;

    private OnItemClickListener mItemClickListener;

    public ImageAdapter(Context context, Cursor cursor) {
        mCtx = context;
        mCursor = cursor;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mCtx).inflate(R.layout.image_rv_item, parent, false);
        ImageViewHolder holder = new ImageViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = ImageResizer.getInstance().decodeSampledBitmapFromFD(fis.getFD(), DimenUtil.dp2px(mCtx, 100), DimenUtil.dp2px(mCtx, 100));
            holder.ivItem.setImageBitmap(bitmap);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, position);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (mCursor != null && mCursor.getCount() > 0) {
            return mCursor.getCount();
        }
        return 0;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItem;

        ImageViewHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.iv_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }


}
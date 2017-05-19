package com.example.liaohaicongsx.coc.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.ImageAdapter;
import com.example.liaohaicongsx.coc.util.NavigationUtil;

/**
 * Author Hancher
 * Date 2017/05/19
 * Contact liaohaicongsx@gomo.com
 * <p/>
 * 图片选择页
 */
public class ImageSelectActivity extends BaseActivity implements ImageAdapter.OnItemClickListener {

    private RecyclerView mRvImage;
    private ImageAdapter mAdapter;
    private Cursor mCursor;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        mRvImage = (RecyclerView) findViewById(R.id.rv_images);
        mRvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mRvImage.setItemAnimator(new DefaultItemAnimator());

        mHandler = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA}, null, null, null);
                mAdapter = new ImageAdapter(ImageSelectActivity.this, mCursor);
                mAdapter.setOnItemClickListener(ImageSelectActivity.this);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mRvImage.setAdapter(mAdapter);
                    }
                });
            }
        }).start();

    }

    @Override
    public String getPageTitle() {
        return getString(R.string.image_select);
    }

    @Override
    public void onItemClick(View view, int position) {
        mCursor.moveToPosition(position);
        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
        NavigationUtil.ImageBackToChatPage(this, path);
    }

    @Override
    protected void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }
}

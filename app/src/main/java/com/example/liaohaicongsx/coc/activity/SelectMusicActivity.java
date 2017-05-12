package com.example.liaohaicongsx.coc.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.MusicAdapter;
import com.example.liaohaicongsx.coc.util.NavigationUtil;

public class SelectMusicActivity extends BaseActivity {

    private ListView mLvMusic;
    private MusicAdapter mAdapter;
    private Cursor mCursor;
    private Handler mHancler;

    @Override
    public String getPageTitle() {
        return getString(R.string.send_music);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);

        mLvMusic = (ListView) findViewById(R.id.lv_music);
        mAdapter = new MusicAdapter(this);
        mLvMusic.setAdapter(mAdapter);

        mHancler = new Handler();

        mLvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCursor.moveToPosition(position);
                String name = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //开启服务，播放音乐
//                Intent intent = new Intent(SelectMusicActivity.this, MusicPlayService.class);
//                intent.putExtra(MusicPlayService.MSG_MUSIC_PATH, path);
//                startService(intent);
                NavigationUtil.backToChatPage(SelectMusicActivity.this, name, path);

            }
        });


        /**
         * 开启异步线程加载音乐文件
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA}, null, null, null);
                mHancler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(mCursor);
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        mCursor.close();  //关闭Cursor
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this, MusicPlayService.class);
//        stopService(intent);
        super.onBackPressed();
    }
}

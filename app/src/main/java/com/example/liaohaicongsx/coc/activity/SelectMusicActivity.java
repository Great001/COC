package com.example.liaohaicongsx.coc.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.MusicAdapter;
import com.example.liaohaicongsx.coc.service.MusicPlayService;

public class SelectMusicActivity extends AppCompatActivity {

    private ListView mLvMusic;
    private MusicAdapter mAdapter;
    private Cursor mCursor;

    private boolean mFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);

        mLvMusic = (ListView) findViewById(R.id.lv_music);
        mAdapter = new MusicAdapter(this);
        mLvMusic.setAdapter(mAdapter);

        mLvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCursor.moveToPosition(position);
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                if (mFirst) {
                    //开启服务，播放音乐
                    Intent intent = new Intent(SelectMusicActivity.this, MusicPlayService.class);
                    intent.putExtra(MusicPlayService.MUSIC_PATH, path);
                    startService(intent);
                    mFirst = false;
                } else {
                    //发送广播，播放音乐
                    Intent broadIntent = new Intent(MusicPlayService.BROADCAST_ACTION);
                    broadIntent.putExtra(MusicPlayService.MUSIC_PATH, path);
                    sendBroadcast(broadIntent);
                }
            }
        });

        /**
         * 开启异步线程加载音乐文件
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA}, null, null, null);
                mAdapter.setData(mCursor);
                mAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(this, MusicPlayService.class);
        stopService(intent);
        super.onBackPressed();
    }
}

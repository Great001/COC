package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.ASResultAdapter;
import com.example.liaohaicongsx.coc.api.RetrofitClient;
import com.example.liaohaicongsx.coc.model.ResponseUserInfos;
import com.example.liaohaicongsx.coc.model.UserInfo;

import org.json.JSONArray;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddFriendActivity extends AppCompatActivity {

    public static final String TAG = "AddFriendActivity";

    private EditText mEtAccount;
    private ListView mLvResults;

    private ASResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mEtAccount = (EditText) findViewById(R.id.et_input_to_add_account);
        mLvResults = (ListView) findViewById(R.id.lv_account_search_result);

        adapter = new ASResultAdapter(this);
        mLvResults.setAdapter(adapter);

        mEtAccount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getUserInfos();
                }
                return false;
            }
        });


    }

    public void getUserInfos() {
        String accid = mEtAccount.getText().toString().trim();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(accid);
        RetrofitClient.getInstance().userGetInfos(jsonArray.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResponseUserInfos>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "错误");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseUserInfos responseUserInfos) {
                        Log.d(TAG, responseUserInfos.toString());
                        final List<UserInfo> userInfoLists = responseUserInfos.getUinfos();

                        adapter.setSearchData(userInfoLists);
                        adapter.notifyDataSetChanged();

                    }
                });
    }
}

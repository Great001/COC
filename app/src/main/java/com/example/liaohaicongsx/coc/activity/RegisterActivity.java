package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.api.RetrofitClient;
import com.example.liaohaicongsx.coc.model.ResponseUser;
import com.example.liaohaicongsx.coc.util.NavigationUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity {

    public static final String TAG = "RegisterActivity";

    private Button mBtnRegister;

    private EditText mEtNickName;
    private EditText mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mEtNickName = (EditText) findViewById(R.id.et_input_nickname);
        mEtPwd = (EditText) findViewById(R.id.et_input_password);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = mEtNickName.getText().toString().trim();
                String password = mEtPwd.getText().toString().trim();
                String account = String.valueOf(System.currentTimeMillis());
                RetrofitClient.getInstance().userRegister(account, nickname)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseUser>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResponseUser responseUser) {
                                Log.d(TAG, responseUser.toString());
                                String token = responseUser.getInfo().getToken();
                                String accid = responseUser.getInfo().getAccid();
                                NavigationUtil.navigateToLoginPage(RegisterActivity.this, accid, token);
                                RegisterActivity.this.finish();
                            }
                        });
            }
        });
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.register_page);
    }
}

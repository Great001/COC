package com.example.liaohaicongsx.coc.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.CocApplication;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.api.RetrofitClient;
import com.example.liaohaicongsx.coc.model.ResponseUser;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnLogin;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTvResult = (TextView) findViewById(R.id.tv_loginResult);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accid = String.valueOf(System.currentTimeMillis());
                RetrofitClient.getInstance().userRegister(accid,null)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<ResponseUser>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(ResponseUser responseUser) {
                                mTvResult.setText(responseUser.toString());
                                final String accid = responseUser.getInfo().getAccid();
                                final String token = responseUser.getInfo().getToken();
                                LoginInfo  loginInfo = new LoginInfo(accid,token);
                                NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback() {
                                    @Override
                                    public void onSuccess(Object param) {
                                        SharedPreferences sp = getSharedPreferences(CocApplication.SP_USER,MODE_PRIVATE);
                                        sp.edit().putString("accid",accid);
                                        sp.edit().putString("token",token);
                                        sp.edit().commit();
                                        NavigationUtil.navigatoMainPage(LoginActivity.this);
                                    }

                                    @Override
                                    public void onFailed(int code) {

                                    }

                                    @Override
                                    public void onException(Throwable exception) {

                                    }
                                });
                            }
                        });
            }
        });
    }
}

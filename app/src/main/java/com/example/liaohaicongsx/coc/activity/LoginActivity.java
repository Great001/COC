package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.util.ToastUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "LoginActivity";

    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PWD = "password";

    private Button mBtnLogin;
    private Button mBtnRegister;
    private Button mBtnFindPwd;

    private EditText mEtAccount;
    private EditText mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        mEtAccount.setText(getIntent().getStringExtra(KEY_ACCOUNT));
        mEtPwd.setText(getIntent().getStringExtra(KEY_PWD));
    }

    public void initView(){
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBtnFindPwd = (Button) findViewById(R.id.btn_find_pwd);
        mEtAccount = (EditText) findViewById(R.id.et_input_account);
        mEtPwd = (EditText) findViewById(R.id.et_input_password);

        mBtnLogin.setOnClickListener(this);
        mBtnFindPwd.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                onLoginClick();
                break;
            case R.id.btn_register:
                onRegisterClick();
                break;
            case R.id.btn_find_pwd:
                onFindPwdClick();
                break;
            default:
                break;

        }

    }

    public void onLoginClick(){
        final String account = mEtAccount.getText().toString().trim();
        final String password = mEtPwd.getText().toString().trim();

        Log.d(TAG,"accout:" + account +"  token:"+password);

        if(!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)){
            LoginInfo loginInfo = new LoginInfo(account,password);
            NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback() {
                @Override
                public void onSuccess(Object param) {
                    UserModel.getInstance(getApplicationContext()).setLoginInfo(account,password);
                    NavigationUtil.navigatoMainPage(LoginActivity.this);
                    LoginActivity.this.finish();
                }

                @Override
                public void onFailed(int code) {
                    ToastUtil.show(LoginActivity.this,R.string.login_fail);
                }

                @Override
                public void onException(Throwable exception) {
                    exception.printStackTrace();
                }
            });
        }else{
            ToastUtil.show(this,R.string.can_not_empty);
        }
    }

    public void onRegisterClick(){
        NavigationUtil.navigateToRegisterPage(this);
        LoginActivity.this.finish();
    }

    public void onFindPwdClick(){
        NavigationUtil.navigateToFindPwdActivity(this);
    }

    @Override
    public void onBackPressed() {
        NavigationUtil.navigatoMainPage(LoginActivity.this);
        UserModel.getInstance(getApplicationContext()).setLoginState(false);
        super.onBackPressed();
    }
}

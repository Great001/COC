package com.example.liaohaicongsx.coc.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.util.ToastUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

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


    @Override
    public String getPageTitle() {
        return getString(R.string.login_page);
    }

    public void initView() {
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
        switch (v.getId()) {
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

    public void onLoginClick() {
        final String account = mEtAccount.getText().toString().trim();
        final String password = mEtPwd.getText().toString().trim();

        Log.d(TAG, "accout:" + account + "  token:" + password);

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            LoginInfo loginInfo = new LoginInfo(account, password);
            NIMClient.getService(AuthService.class).login(loginInfo).setCallback(new RequestCallback() {
                @Override
                public void onSuccess(Object param) {
                    UserModel.getInstance().setLoginInfo(getApplicationContext(), account, password);
                    UserModel.getInstance().queryUserInfo(account);
                    NavigationUtil.navigateToMainPage(LoginActivity.this);
                    LoginActivity.this.finish();
                }

                @Override
                public void onFailed(int code) {
                    ToastUtil.show(LoginActivity.this, R.string.login_fail);
                }

                @Override
                public void onException(Throwable exception) {
                    // TODO: 17/5/8 用户提示
                    exception.printStackTrace();
                }
            });
        } else {
            ToastUtil.show(this, R.string.can_not_empty);
        }
    }

    public void onRegisterClick() {
        NavigationUtil.navigateToRegisterPage(this);
        LoginActivity.this.finish();
    }

    public void onFindPwdClick() {
        NavigationUtil.navigateToFindPwdActivity(this);
    }

    @Override
    public void onBackPressed() {
        AppActivityManager.getAppActivityManager().clear();
        System.exit(1);
        super.onBackPressed();
    }
}

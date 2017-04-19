package com.example.liaohaicongsx.coc.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by liaohaicongsx on 2017/04/19.
 */
public class UserModel {

    private volatile static UserModel instance;

    public static final String SP_USER = "userinfo";
    public static final String KEY_ACCID = "accid";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_LOGIN_STATE = "loginState";

    private Context mCtx;
    private boolean mLoginState;

    public UserModel(Context context){
        mCtx = context;
    }

    public static UserModel getInstance(Context context){
        if(instance == null){
            synchronized (UserModel.class){
                instance = new UserModel(context);
            }
        }
        return instance;
    }

    public LoginInfo getLoginInfo(){
        final SharedPreferences sp = mCtx.getSharedPreferences(SP_USER,Context.MODE_PRIVATE);
        String account = sp.getString(KEY_ACCID, null);
        String token = sp.getString(KEY_TOKEN, null);
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            mLoginState = true;
            sp.edit().putBoolean(KEY_LOGIN_STATE, true).commit();
            return new LoginInfo(account, token);
        } else {
            mLoginState = false;
            sp.edit().putBoolean(KEY_LOGIN_STATE, false).commit();
            return null;
        }
    }

    public void setLoginInfo(String account,String token){
        SharedPreferences sp = mCtx.getSharedPreferences(SP_USER,Context.MODE_APPEND);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_ACCID,account);
        editor.putString(KEY_TOKEN,token);
        editor.putBoolean(KEY_LOGIN_STATE,true);
        editor.commit();
        setLoginState(true);
    }

    public boolean getLoginState() {
        return mLoginState;
    }

    public void setLoginState(boolean mLoginState) {
        this.mLoginState = mLoginState;
    }
}

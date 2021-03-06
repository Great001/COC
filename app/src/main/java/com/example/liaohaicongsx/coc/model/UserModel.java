package com.example.liaohaicongsx.coc.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.liaohaicongsx.coc.api.RetrofitClient;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONArray;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liaohaicongsx on 2017/04/19.
 */
public class UserModel {

    public static final String TAG = "UserModel";

    private volatile static UserModel sInstance;

    public static final String SP_USER = "userinfo";

    public static final String KEY_ACCID = "accid";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_LOGIN_STATE = "loginState";

    private boolean mLoginState;
    private UserInfo mUserInfo;

    public static UserModel getInstance() {
        if (sInstance == null) {
            synchronized (UserModel.class) {
                sInstance = new UserModel();
            }
        }
        return sInstance;
    }

    public LoginInfo getLoginInfo(Context context) {
        final SharedPreferences sp = context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE);
        String account = sp.getString(KEY_ACCID, null);
        String token = sp.getString(KEY_TOKEN, null);
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            sp.edit().putBoolean(KEY_LOGIN_STATE, true).apply();
            setLoginState(true);
            return new LoginInfo(account, token);
        } else {
            sp.edit().putBoolean(KEY_LOGIN_STATE, false).apply();
            setLoginState(false);
            return null;
        }
    }

    public void setLoginInfo(Context context, String account, String token) {
        SharedPreferences sp = context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_ACCID, account);
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_LOGIN_STATE, true);
        editor.apply();
        setLoginState(true);
    }

    public boolean getLoginState() {
        return mLoginState;
    }

    public void setLoginState(boolean mLoginState) {
        this.mLoginState = mLoginState;
    }

    public void queryUserInfo(String accid) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(accid);  //参数封装成JsonArray的格式
        RetrofitClient.getInstance().userGetInfos(jsonArray.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseUserInfos>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseUserInfos responseUserInfos) {
                        mUserInfo = responseUserInfos.getUinfos().get(0);
                        Log.d(TAG, mUserInfo.toString());
                    }
                });

    }

    public UserInfo getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        } else {
            return new UserInfo("ff0e8e13ddf903de1b077434a28457a6", "1492596274543", "test001");  //构造假数据
        }
    }

}

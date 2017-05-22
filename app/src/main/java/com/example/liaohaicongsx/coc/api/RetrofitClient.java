package com.example.liaohaicongsx.coc.api;


import com.example.liaohaicongsx.coc.api.interfaces.UserGetInfo;
import com.example.liaohaicongsx.coc.api.interfaces.UserRegister;
import com.example.liaohaicongsx.coc.model.ResponseUser;
import com.example.liaohaicongsx.coc.model.ResponseUserInfos;
import com.example.liaohaicongsx.coc.util.CheckSumBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class RetrofitClient {

    public static final String TAG = "RetrofitClient";

    public static final String BASE_URL = "https://api.netease.im/nimserver/";

    private volatile static RetrofitClient sInstance;

    public Retrofit retrofit;

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (sInstance == null) {
            synchronized (RetrofitClient.class) {
                sInstance = new RetrofitClient();
            }
        }
        return sInstance;
    }

    //用户注册
    public Observable<ResponseUser> userRegister(String accid, String name) {
        UserRegister userRegister = retrofit.create(UserRegister.class);
        return userRegister.register(getHeaderMap(), accid, name);
    }

    //用户账户搜索
    public Observable<ResponseUserInfos> userGetInfos(String accid) {
        UserGetInfo uInfos = retrofit.create(UserGetInfo.class);
        return uInfos.getUserInfos(getHeaderMap(), accid);

    }


    //header处理
    public Map<String, String> getHeaderMap() {
        Map<String, String> headMap = new HashMap<>();

        String appKey = "bc8c6eff342a2199e9d0abd58ce36f1f";
        String appSecret = "c1e5eb81cc9f";
        String nonce = String.valueOf(new Random().nextFloat());
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);

        headMap.put("AppKey", appKey);
        headMap.put("Nonce", nonce);
        headMap.put("CurTime", curTime);
        headMap.put("CheckSum", checkSum);
        headMap.put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headMap;
    }


}

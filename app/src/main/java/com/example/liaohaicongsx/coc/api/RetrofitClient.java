package com.example.liaohaicongsx.coc.api;


import com.example.liaohaicongsx.coc.api.interfaces.UserRegister;
import com.example.liaohaicongsx.coc.model.ResponseUser;
import com.example.liaohaicongsx.coc.util.CheckSumBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class RetrofitClient {

    public static final String TAG = "RetrofitClient";

    public static final String BASE_URL = "https://api.netease.im/nimserver/";

    private volatile static RetrofitClient instance;

    public Retrofit retrofit;

    public RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                instance = new RetrofitClient();
            }
        }
        return instance;
    }

    //用户注册
    public Observable<ResponseUser> userRegister(String accid,String name) {
        UserRegister userRegister = retrofit.create(UserRegister.class);
        return userRegister.register(getHeaderMap(),accid,name);
    }


    //header处理
    public Map<String, String> getHeaderMap() {
        Map<String, String> headMap = new HashMap<>();

        String appKey = "94c68366b84a44de04bf7c5d98689423";
        String appSecret = "3c15b0d1dd31";
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

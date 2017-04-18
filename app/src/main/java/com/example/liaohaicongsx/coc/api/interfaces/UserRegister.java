package com.example.liaohaicongsx.coc.api.interfaces;

import com.example.liaohaicongsx.coc.model.ResponseUser;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public interface UserRegister {
    @FormUrlEncoded
    @POST("user/create.action")
    Observable<ResponseUser> register(@HeaderMap() Map<String, String> headMap, @Field("accid") String acccid,@Field("name") String name);
}

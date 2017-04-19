package com.example.liaohaicongsx.coc.api.interfaces;

import com.example.liaohaicongsx.coc.model.ResponseUser;
import com.example.liaohaicongsx.coc.model.ResponseUserInfos;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liaohaicongsx on 2017/04/19.
 */
public interface UserGetInfo {

    @FormUrlEncoded
    @POST("user/getUinfos.action")
    Observable<ResponseUserInfos> getUserInfos(@HeaderMap()Map<String,String> headerMap, @Field("accids") String accid);
}

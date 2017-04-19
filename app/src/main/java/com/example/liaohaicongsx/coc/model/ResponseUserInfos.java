package com.example.liaohaicongsx.coc.model;

import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/19.
 */
public class ResponseUserInfos {

    private int code;

    private List<UserInfo> uinfos;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<UserInfo> getUinfos() {
        return uinfos;
    }

    public void setUinfos(List<UserInfo> uinfos) {
        this.uinfos = uinfos;
    }


    @Override
    public String toString() {
        return "ResponseUserInfos{" +
                "code=" + code +
                ", uinfos=" + uinfos +
                '}';
    }
}

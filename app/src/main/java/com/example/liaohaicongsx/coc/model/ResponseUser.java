package com.example.liaohaicongsx.coc.model;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class ResponseUser {

    private String code;
    private UserInfo info;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public ResponseUser(String code, UserInfo info) {
        this.code = code;
        this.info = info;
    }

    @Override
    public String toString() {
        return "ResponseUser{" +
                "code='" + code + '\'' +
                ", info=" + info +
                '}';
    }
}

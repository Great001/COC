package com.example.liaohaicongsx.coc.model;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class ResponseUser {

    private int code;
    private UserInfo info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public ResponseUser(int code, UserInfo info) {
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

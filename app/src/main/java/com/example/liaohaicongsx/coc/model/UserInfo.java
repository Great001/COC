package com.example.liaohaicongsx.coc.model;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class UserInfo {

    private String token;
    private String accid;
    private String name;

    public UserInfo(String token, String accid, String name) {
        this.token = token;
        this.accid = accid;
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "token='" + token + '\'' +
                ", accid='" + accid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

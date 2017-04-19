package com.example.liaohaicongsx.coc.model;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class UserInfo {

    private String token;
    private String accid;
    private String name;

    private String mobile;
    private String email;
    private String signature;
    private String avaterUrl;


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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvaterUrl() {
        return avaterUrl;
    }

    public void setAvaterUrl(String avaterUrl) {
        this.avaterUrl = avaterUrl;
    }

    @Override
    public String toString() {
        return "UserGetInfo{" +
                "token='" + token + '\'' +
                ", accid='" + accid + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", signature='" + signature + '\'' +
                ", avaterUrl='" + avaterUrl + '\'' +
                '}';
    }
}

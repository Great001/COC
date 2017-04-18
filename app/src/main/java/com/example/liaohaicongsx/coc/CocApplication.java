package com.example.liaohaicongsx.coc;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class CocApplication extends Application {
    public static final String SP_USER = "userinfo";
    @Override
    public void onCreate() {
        super.onCreate();
        NIMClient.init(getApplicationContext(),getLoginInfo(),getSDKOptions());
    }

    public LoginInfo getLoginInfo(){
        final SharedPreferences sp = getSharedPreferences(SP_USER,MODE_PRIVATE);
        String account = sp.getString("accid",null);
        String token = sp.getString("token",null);
        if(!TextUtils.isEmpty(account )&&!TextUtils.isEmpty(token)){
            sp.edit().putBoolean("loginState",true).commit();
            return new LoginInfo(account,token);
        }else {
            sp.edit().putBoolean("loginState",false).commit();
            return null;
        }
    }

    public SDKOptions getSDKOptions(){
        SDKOptions options = new SDKOptions();
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        options.userInfoProvider = new UserInfoProvider(){
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return 0;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }
        };
        return options;
    }

}

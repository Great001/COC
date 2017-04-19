package com.example.liaohaicongsx.coc;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;

import com.example.liaohaicongsx.coc.model.UserModel;
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

    @Override
    public void onCreate() {
        super.onCreate();

        //获取用户的accid和token，自动登录
        LoginInfo loginInfo = UserModel.getInstance(getApplicationContext()).getLoginInfo();
        NIMClient.init(getApplicationContext(), loginInfo, getSDKOptions());
    }


    public SDKOptions getSDKOptions() {
        SDKOptions options = new SDKOptions();
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        options.userInfoProvider = new UserInfoProvider() {
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

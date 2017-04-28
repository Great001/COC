package com.example.liaohaicongsx.coc;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.example.liaohaicongsx.coc.activity.AddFriendVefifyActivity;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class CocApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //获取用户的accid和token，自动登录
        LoginInfo loginInfo = UserModel.getInstance().getLoginInfo(getApplicationContext());
        NIMClient.init(this, loginInfo, getSDKOptions());

        //加载用户资料
        if (loginInfo != null) {
            UserModel.getInstance().queryUserInfo(loginInfo.getAccount());
        }
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

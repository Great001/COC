package com.example.liaohaicongsx.coc;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;

import com.example.liaohaicongsx.coc.activity.AddFriendVefifyActivity;
import com.example.liaohaicongsx.coc.activity.ChatActivity;
import com.example.liaohaicongsx.coc.activity.MainActivity;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.SystemUtil;
import com.example.liaohaicongsx.coc.view.NotifyFloatingView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.List;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class CocApplication extends Application {

    public static final int ID_IM_NOTIFICATION = 0;

    /**
     * 消息通知悬浮窗
     */
    private NotifyFloatingView mNotifyFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();

        //获取用户的accid和token，自动登录
        LoginInfo loginInfo = UserModel.getInstance().getLoginInfo(getApplicationContext());

        //SDK初始化
        NIMClient.init(this, loginInfo, getSDKOptions());

        //加载用户资料
        if (loginInfo != null) {
            UserModel.getInstance().queryUserInfo(loginInfo.getAccount());
        }

        //等待主进程，进行监听服务注册
        if (inMainProcess(this)) {
            mNotifyFloatingView = new NotifyFloatingView(this);
            registerSystemMsgObserver();   //监听系统通知
            registerImMsgObserver();       //监听IM消息通知
        }
    }


    /**
     * 获取网易云信SDK的配置选项
     */
    public SDKOptions getSDKOptions() {
        SDKOptions options = new SDKOptions();

        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

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


    /**
     * 注册系统通知监听器
     */
    public void registerSystemMsgObserver() {
        NIMClient.getService(SystemMessageObserver.class)
                .observeReceiveSystemMsg(new Observer<SystemMessage>() {
                    @Override
                    public void onEvent(SystemMessage message) {
                        //主要监听好友添加相关的通知
                        if (message.getType() == SystemMessageType.AddFriend) {
                            dealWithAddFriendMsg(message);
                        }
                    }
                }, true);
    }

    /**
     * 注册IM消息监听器
     */
    public void registerImMsgObserver() {
        Observer<List<IMMessage>> incomingMsgObserver = new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                Activity topActivity = AppActivityManager.getAppActivityManager().topActivity();
                if (topActivity instanceof ChatActivity && ((ChatActivity) topActivity).isForground()) {
                    //当前已经是聊天页面，不弹出新消息通知
                } else {
                    showImMsgNotification(imMessages);
                    showNotifyWindow(imMessages.get(0));
                }
                if (topActivity instanceof MainActivity && ((MainActivity) topActivity).getCurrentItem() == 0) {
                    //使用广播发送消息，进行更新
                    Intent intent = new Intent("com.example.liaohaicongsx.coc.RecentContact.update");
                    sendBroadcast(intent);
                }
            }
        };
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver, true);
    }


    /**
     * 处理好友添加消息
     *
     * @param message
     */
    public void dealWithAddFriendMsg(SystemMessage message) {
        AddFriendNotify attachData = (AddFriendNotify) message.getAttachObject();
        if (attachData != null) {
            // 针对不同的事件做处理
            if (attachData.getEvent() == AddFriendNotify.Event.RECV_AGREE_ADD_FRIEND) {
                // 对方通过了你的好友验证请求
                String action = "com.example.liaohaicongsx.coc.chatActivity";
                showAddFriendNotification(R.string.accept_new_friend_request, message, action);
            } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_REJECT_ADD_FRIEND) {
                // 对方拒绝了你的好友验证请求
            } else {
                if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                    // 对方请求添加好友，一般场景会让用户选择同意或拒绝对方的好友请求。
                    String action = "com.example.liaohaicongsx.coc.addFriendVerify";
                    showAddFriendNotification(R.string.add_friend_request, message, action);
                }
            }
        }
    }

    /**
     * 显示好友添加请求通知
     *
     * @param contentTitle
     * @param message
     * @param action
     */
    public void showAddFriendNotification(int contentTitle, SystemMessage message, String action) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setContentTitle(getResources().getString(contentTitle));
        builder.setContentText(message.getContent());
        Intent intent = new Intent(action);
        intent.putExtra(AddFriendVefifyActivity.KEY_SYSTEM_MSG, message);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        nm.notify(0, builder.build());
    }

    /**
     * 接收到IM消息时通知栏显示
     *
     * @param imMessages
     */
    public void showImMsgNotification(List<IMMessage> imMessages) {
        IMMessage message = imMessages.get(0);
        String fromAccount = message.getFromAccount();
        String fromNick = message.getFromNick();
        String messageContent = message.getContent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setContentTitle(getString(R.string.news_comming));
        builder.setContentText(messageContent);
        Intent intent = new Intent("com.example.liaohaicongsx.coc.chatActivity");
        intent.putExtra(ChatActivity.NICK, fromNick);
        intent.putExtra(ChatActivity.ACCOUNT, fromAccount);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(ID_IM_NOTIFICATION, builder.build());
    }

    /**
     * 接收到消息显示悬浮通知弹窗
     *
     * @param msg
     */
    public void showNotifyWindow(final IMMessage msg) {
        if (mNotifyFloatingView != null) {
            mNotifyFloatingView.setOnNotifyClickListener(new NotifyFloatingView.OnNotifyClickListener() {
                @Override
                public void onNotifyClick() {
                    Intent intent = new Intent("com.example.liaohaicongsx.coc.chatActivity");
                    intent.putExtra(ChatActivity.ACCOUNT, msg.getFromAccount());
                    intent.putExtra(ChatActivity.NICK, msg.getFromNick());
                    AppActivityManager.getAppActivityManager().topActivity().startActivity(intent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(ID_IM_NOTIFICATION);
                }
            });
            mNotifyFloatingView.show(msg);
        }
    }


    /**
     * 判断当前是否在应用主进程
     *
     * @param context
     * @return
     */
    public boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = SystemUtil.getProcessName(context);
        return packageName.equals(processName);
    }


}

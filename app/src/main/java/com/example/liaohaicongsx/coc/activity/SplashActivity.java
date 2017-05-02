package com.example.liaohaicongsx.coc.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.List;
import java.util.logging.Handler;

public class SplashActivity extends AppCompatActivity {

    private Button mBtnEnter;   //进入首页按钮
    private ImageView mIvAd;  //展示广告

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBtnEnter = (Button) findViewById(R.id.btn_enter_main_page);
        mIvAd = (ImageView) findViewById(R.id.iv_splash);

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserModel.getInstance().getLoginState()) {
                    NavigationUtil.navigatoMainPage(SplashActivity.this);
                } else {
                    NavigationUtil.navigateToLoginPage(SplashActivity.this);
                }
                finish();
            }
        });

        registerSystemMsgObserver();   //监听系统通知
        registerImMsgObserver();       //监听IM消息
    }

    public void registerSystemMsgObserver() {
        //监听系统消息
        NIMClient.getService(SystemMessageObserver.class)
                .observeReceiveSystemMsg(new Observer<SystemMessage>() {
                    @Override
                    public void onEvent(SystemMessage message) {
                        if (message.getType() == SystemMessageType.AddFriend) {
                            dealWithAddFriendMsg(message);
                        }
                    }
                }, true);
    }

    public void registerImMsgObserver() {
        //监听IM消息
        Observer<List<IMMessage>> incomingMsgObserver = new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                if (AppActivityManager.getInstance().topActivity() instanceof ChatActivity) {
                    //当前已经是聊天页面，不弹出新消息通知
                } else {
                    showImMsgNotification(imMessages);
                    showNotifyWindow();
                }
            }
        };
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver, true);
    }


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

    public void showAddFriendNotification(int contentTitle, SystemMessage message, String action) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setContentTitle(getResources().getString(contentTitle));
        builder.setContentText(message.getContent());
        Intent intent = new Intent(action);
        intent.putExtra(ChatActivity.ACCOUNT,message.getFromAccount());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        nm.notify(0, builder.build());
    }

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
        intent.putExtra(ChatActivity.ACCOUNT,fromAccount);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
    }

    public void showNotifyWindow(){

        final WindowManager windowManager = getWindowManager();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.format = PixelFormat.RGBA_8888;
        params.y = 0;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final View view = LayoutInflater.from(this).inflate(R.layout.notifyview,null);
        windowManager.addView(view,params);

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                windowManager.removeView(view);
            }
        },3000);
    }

}

package com.example.liaohaicongsx.coc.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.model.UserModel;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.util.ToastUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.model.AddFriendNotify;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private Button mBtnEnter;
    private ImageView mIvAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mBtnEnter = (Button) findViewById(R.id.btn_enter_main_page);
        mIvAd = (ImageView) findViewById(R.id.iv_splash);

        mBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserModel.getInstance().getLoginState()) {
                    NavigationUtil.navigatoMainPage(SplashActivity.this);
                }else{
                    NavigationUtil.navigateToLoginPage(SplashActivity.this);
                }
                finish();
            }
        });

        registerSystemMsgObserver();
        registerImMsgObserver();

    }

    public void registerSystemMsgObserver(){
        //监听系统事件
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

    public void registerImMsgObserver(){
        //监听IM消息
        Observer<List<IMMessage>> incomingMsgObserver = new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                if(AppActivityManager.getInstance().topActivity() instanceof  ChatActivity ){

                }else {
                  showImMsgNotification(imMessages);
                }
            }
        };
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver,true);
    }



    public void dealWithAddFriendMsg(SystemMessage message) {
        AddFriendNotify attachData = (AddFriendNotify) message.getAttachObject();
        if (attachData != null) {
            // 针对不同的事件做处理
            if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_DIRECT) {
                // 对方直接添加你为好友
            } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_AGREE_ADD_FRIEND) {
                // 对方通过了你的好友验证请求
                String action = "com.example.liaohaicongsx.coc.chatActivity";
                showAddFriendNotification(R.string.accept_new_friend_request,message,action);
            } else if (attachData.getEvent() == AddFriendNotify.Event.RECV_REJECT_ADD_FRIEND) {
                // 对方拒绝了你的好友验证请求
            } else {
                if (attachData.getEvent() == AddFriendNotify.Event.RECV_ADD_FRIEND_VERIFY_REQUEST) {
                    // 对方请求添加好友，一般场景会让用户选择同意或拒绝对方的好友请求。
                    // 通过message.getContent()获取好友验证请求的附言
                    String action = "com.example.liaohaicongsx.coc.addFriendVerify";
                    showAddFriendNotification(R.string.add_friend_request,message,action);
                }
            }
        }
    }

    public void showAddFriendNotification(int contentTitle, SystemMessage message, String action){
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setContentTitle(getResources().getString(contentTitle));
        builder.setContentText(message.getContent());
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        nm.notify(0,builder.build());
    }

    public void showImMsgNotification(List<IMMessage> imMessages){
        IMMessage message = imMessages.get(0);
        String fromName = message.getFromNick();
        String messageContent = message.getContent();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.app_logo);
        builder.setContentTitle(getString(R.string.news_comming));
        builder.setContentText(messageContent);
        Intent intent  = new Intent("com.example.liaohaicongsx.coc.chatActivity");
        intent.putExtra("message",message);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,builder.build());

    }

}

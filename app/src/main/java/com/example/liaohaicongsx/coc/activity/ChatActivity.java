package com.example.liaohaicongsx.coc.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.ChatAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.SystemMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {

    public static final String ACCOUNT = "account";

    private RelativeLayout mRlRoot;
    private ListView mLvChat;
    private EditText mEtMsg;
    private Button mBtnSend;

    private ImageView mIvEmoji;

    private ChatAdapter mAdapter;
    private List<IMMessage> mMsgList = new ArrayList<>();

    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        IMMessage message = (IMMessage) getIntent().getSerializableExtra("message");
        if (message != null) {
            account = message.getFromAccount();
            mMsgList.add(message);
        } else {
            account = getIntent().getStringExtra(ACCOUNT);
        }

        initView();
        //查询历史纪录
        queryOldMsgRecords();
        //接收消息
        observeRecvMessage();
    }

    @Override
    public String getPageTitle() {
        return getString(R.string.chat_page);
    }

    public void initView() {
        mRlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mLvChat = (ListView) findViewById(R.id.lv_chat);
        mEtMsg = (EditText) findViewById(R.id.et_input_message);
        mBtnSend = (Button) findViewById(R.id.btn_send_message);
        mIvEmoji = (ImageView) findViewById(R.id.iv_msg_emoji);

        mEtMsg.clearFocus();

        mAdapter = new ChatAdapter(this);
        mAdapter.setMessages(mMsgList);
        mLvChat.setAdapter(mAdapter);

        mEtMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 0) {
                    mBtnSend.setClickable(true);
                    mBtnSend.setBackgroundResource(R.drawable.bg_btn_chat_send);
                } else {
                    mBtnSend.setClickable(false);
                    mBtnSend.setBackgroundResource(R.drawable.bg_btn_chat_send_not);
                }
            }
        });

        mIvEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Html.ImageGetter imageGetter = new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        Drawable drawable = getResources().getDrawable(Integer.valueOf(source));
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        return drawable;
                    }
                };

                String messageContent = Html.toHtml(mEtMsg.getText());
                int start = messageContent.indexOf(">") + 1;
                int end = messageContent.lastIndexOf("</p>");
                if (end != -1) {
                    messageContent = messageContent.substring(start, end);
                    Log.d("ChatActivity", messageContent);
                }

                String img = "<img src='" + R.drawable.emoji + "'/>";
                messageContent += img;
                Spanned html = Html.fromHtml(messageContent, imageGetter, null);
//                Log.d("ChatActivity",html.toString());
                mEtMsg.setText(html);
            }
        });

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spanned messageContent = mEtMsg.getText();
                String message = Html.toHtml(messageContent);
                int start = message.indexOf(">") + 1;
                int end = message.lastIndexOf("</p>");
                message = message.substring(start, end);
                Log.d("ChatActivity", message);
                //发送消息
                sendMsg(message);
            }


            public void sendMsg(String msg) {
                IMMessage message = MessageBuilder.createTextMessage(account, SessionTypeEnum.P2P, msg);
                mMsgList.add(message);
                mAdapter.setMessages(mMsgList);
                mAdapter.notifyDataSetChanged();
                mEtMsg.setText("");
                NIMClient.getService(MsgService.class).sendMessage(message, true).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                    }

                    @Override
                    public void onFailed(int code) {
                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
                mLvChat.setSelection(mAdapter.getCount() - 1);
            }
        });
    }


    public void observeRecvMessage() {
        Observer<List<IMMessage>> incomingMsgObserver = new Observer<List<IMMessage>>() {
            @Override
            public void onEvent(List<IMMessage> imMessages) {
                mMsgList.addAll(imMessages);
                mAdapter.setMessages(mMsgList);
                mAdapter.notifyDataSetChanged();
                mLvChat.setSelection(mAdapter.getCount() - 1);
            }
        };
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(incomingMsgObserver, true);
    }


    public void queryOldMsgRecords() {
        IMMessage emptyMsg = MessageBuilder.createEmptyMessage(account, SessionTypeEnum.P2P, System.currentTimeMillis());
        NIMClient.getService(MsgService.class).queryMessageListEx(emptyMsg, QueryDirectionEnum.QUERY_OLD, 50, true).setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> param) {
                mMsgList.addAll(0, param);
                mAdapter.setMessages(mMsgList);
                mAdapter.notifyDataSetChanged();
                mLvChat.setSelection(mAdapter.getCount() - 1);
            }

            @Override
            public void onFailed(int code) {

            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mRlRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                InputMethodManager  im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                im.hideSoftInputFromWindow(mRlRoot.getWindowToken(),InputMethodManager.RESULT_HIDDEN);
            }
        });
    }


}
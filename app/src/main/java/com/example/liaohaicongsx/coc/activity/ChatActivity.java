package com.example.liaohaicongsx.coc.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.ChatAdapter;
import com.example.liaohaicongsx.coc.util.ToastUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String ACCOUNT = "account";
    public static final String NICK = "nick";

    public static final int HIDE_WINDOW = 1;

    private RelativeLayout mRlRoot;
    private ListView mLvChat;
    private EditText mEtMsg;
    private Button mBtnSend;

    private ImageView mIvEmoji;

    private ChatAdapter mAdapter;
    private List<IMMessage> mMsgList = new ArrayList<>();

    private String account;
    private String nick;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HIDE_WINDOW:
                    hideWindow();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        account = getIntent().getStringExtra(ACCOUNT);
        nick = getIntent().getStringExtra(NICK);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(nick);
        }

        AppActivityManager.getInstance().push(this);

        initView();

        textView = new TextView(ChatActivity.this);
        textView.setBackgroundColor(getResources().getColor(R.color.white));
        textView.setText("发送成功");
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setGravity(Gravity.CENTER);

        queryOldMsgRecords();    //查询历史纪录
        observeRecvMessage();    //接收消息
    }


    public void initView() {
        mRlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mLvChat = (ListView) findViewById(R.id.lv_chat);
        mEtMsg = (EditText) findViewById(R.id.et_input_message);
        mBtnSend = (Button) findViewById(R.id.btn_send_message);
        mIvEmoji = (ImageView) findViewById(R.id.iv_msg_emoji);

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

                String emoji = "<img src='" + R.drawable.emoji + "'/>";
                messageContent += emoji;
                Spanned html = Html.fromHtml(messageContent, imageGetter, null);
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
                if (end != -1) {
                    message = message.substring(start, end);
                    Log.d("ChatActivity", message);
                    //发送消息
                    sendMsg(message);
                }
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
                        //悬浮窗实现
//                       showWindow();
                        showNotifyWindow();
                    }

                    @Override
                    public void onFailed(int code) {
                        ToastUtil.show(ChatActivity.this, "发送失败");
                    }

                    @Override
                    public void onException(Throwable exception) {
                        exception.printStackTrace();

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
        NIMClient.getService(MsgService.class).queryMessageListEx(emptyMsg, QueryDirectionEnum.QUERY_OLD, 50, true).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                mMsgList.addAll(0, result);
                mAdapter.setMessages(mMsgList);
                mAdapter.notifyDataSetChanged();
                mLvChat.setSelection(mAdapter.getCount() - 1);
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


    @Override
    public void onBackPressed() {
        AppActivityManager.getInstance().pop();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showWindow(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 150;
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        wm.addView(textView,params);
        handler.sendEmptyMessageDelayed(HIDE_WINDOW,3000);
    }


    public void hideWindow(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(textView);
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
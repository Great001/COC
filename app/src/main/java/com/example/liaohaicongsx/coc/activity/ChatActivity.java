package com.example.liaohaicongsx.coc.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.liaohaicongsx.coc.AppActivityManager;
import com.example.liaohaicongsx.coc.R;
import com.example.liaohaicongsx.coc.adapter.ChatAdapter;
import com.example.liaohaicongsx.coc.util.NavigationUtil;
import com.example.liaohaicongsx.coc.util.ToastUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天界面
 */
public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ACCOUNT = "account";
    public static final String NICK = "nick";

    public static final String MSG_MUSIC_PATH = "music_path";
    public static final String MSG_MUSIC_NAME = "music_name";

    public static final String MSG_IMAGE_PATH = "image_path";

    private RelativeLayout mRlRoot;
    private ListView mLvChat;
    private EditText mEtMsg;
    private Button mBtnSend;

    private ImageView mIvEmoji;
    private ImageView mIvMusic;
    private ImageView mIvImage;

    private ChatAdapter mAdapter;
    private List<IMMessage> mMsgList = new ArrayList<>();

    private String mAccount;
    private String mNick;

    private boolean mIsForground;

    private Observer<List<IMMessage>> mIncomingMsgObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(final List<IMMessage> imMessages) {
            for (final IMMessage message : imMessages) {
                if (message.getMsgType() == MsgTypeEnum.file && !isFileAttachmentExists(message)) {
                    //自动下载附件文件
                    NIMClient.getService(MsgService.class).downloadAttachment(message, false).setCallback(new RequestCallbackWrapper() {
                        @Override
                        public void onResult(int code, Object result, Throwable exception) {
                            if (code == 200) {
                                updateMessage(message);
                            }
                        }
                    });
                } else if (message.getMsgType() == MsgTypeEnum.image && !isImageAttachmentExists(message)) {
                    NIMClient.getService(MsgService.class).downloadAttachment(message, false).setCallback(new RequestCallbackWrapper() {
                        @Override
                        public void onResult(int code, Object result, Throwable exception) {
                            if (code == 200) {
                                updateMessage(message);
                            }
                        }
                    });
                } else {
                    updateMessage(message);
                }
            }


        }
    };


    /**
     * 接收到消息时更新显示
     *
     * @param message
     */
    public void updateMessage(IMMessage message) {
        mMsgList.add(message);
        mAdapter.setIMMessages(mMsgList);
        mAdapter.notifyDataSetChanged();
        mLvChat.setSelection(mAdapter.getCount() - 1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAccount = getIntent().getStringExtra(ACCOUNT);
        mNick = getIntent().getStringExtra(NICK);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle(mNick);
        }
        AppActivityManager.getAppActivityManager().push(this);

        initView();

        queryOldMsgRecords();
        observeRecvMessage();
    }

    public void initView() {
        mRlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        mLvChat = (ListView) findViewById(R.id.lv_chat);
        mEtMsg = (EditText) findViewById(R.id.et_input_message);
        mBtnSend = (Button) findViewById(R.id.btn_send_message);
        mIvEmoji = (ImageView) findViewById(R.id.iv_msg_emoji);
        mIvMusic = (ImageView) findViewById(R.id.iv_msg_music);
        mIvImage = (ImageView) findViewById(R.id.iv_msg_img);

        mIvEmoji.setOnClickListener(this);
        mIvMusic.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mIvImage.setOnClickListener(this);

        mAdapter = new ChatAdapter(this);
        mAdapter.setIMMessages(mMsgList);
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
                    mBtnSend.setBackgroundResource(R.drawable.bg_btn_chat_send_able);
                } else {
                    mBtnSend.setClickable(false);
                    mBtnSend.setBackgroundResource(R.drawable.bg_btn_chat_send_not_able);
                }
            }
        });
    }

    /**
     * 监听处理收到的IM消息
     */
    public void observeRecvMessage() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(mIncomingMsgObserver, true);
    }


    /**
     * 判断当前消息附件是否已经存在
     *
     * @param message 接收到文件附件消息
     * @return
     */
    private boolean isFileAttachmentExists(IMMessage message) {
        if (message.getAttachStatus() == AttachStatusEnum.transferred &&
                !TextUtils.isEmpty(((FileAttachment) message.getAttachment()).getPath())) {
            return true;
        }
        return false;
    }

    private boolean isImageAttachmentExists(IMMessage message) {
        if (message.getAttachStatus() == AttachStatusEnum.transferred &&
                !TextUtils.isEmpty(((ImageAttachment) message.getAttachment()).getPath())) {
            return true;
        }
        return false;
    }


    /**
     * 查询历史消息记录
     */
    public void queryOldMsgRecords() {
        IMMessage emptyMsg = MessageBuilder.createEmptyMessage(mAccount, SessionTypeEnum.P2P, System.currentTimeMillis());
        NIMClient.getService(MsgService.class).queryMessageListEx(emptyMsg, QueryDirectionEnum.QUERY_OLD, 50, true).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                mMsgList.addAll(0, result);
                mAdapter.setIMMessages(mMsgList);
                mAdapter.notifyDataSetChanged();
                mLvChat.setSelection(mAdapter.getCount() - 1);
            }
        });
    }


    /**
     * 发送消息
     *
     * @param msg html格式化的文本消息
     */
    public void sendMsg(String msg) {
        final IMMessage message = MessageBuilder.createTextMessage(mAccount, SessionTypeEnum.P2P, msg);
        mMsgList.add(message);
        mAdapter.setIMMessages(mMsgList);
        mAdapter.notifyDataSetChanged();
        mEtMsg.setText("");
        NIMClient.getService(MsgService.class).sendMessage(message, true).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int code, Void result, Throwable exception) {

            }
        });
        mLvChat.setSelection(mAdapter.getCount() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setIsForground(true);
    }


    @Override
    public void onBackPressed() {
        AppActivityManager.getAppActivityManager().pop();
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


    @Override
    protected void onStop() {
        super.onStop();
        setIsForground(false);
        NIMClient.getService(MsgService.class).clearUnreadCount(mAccount, SessionTypeEnum.P2P);
    }

    public boolean isForground() {
        return mIsForground;
    }

    public void setIsForground(boolean mIsForground) {
        this.mIsForground = mIsForground;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg_emoji:
                onEmojiClick();
                break;
            case R.id.iv_msg_music:
                onMusicClick();
                break;
            case R.id.iv_msg_img:
                onImageClick();
                break;
            case R.id.btn_send_message:
                onSendClick();
                break;
            default:
                break;

        }
    }

    /**
     * 点击表情输入
     */
    public void onEmojiClick() {
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
        mEtMsg.setSelection(html.length());
    }

    /**
     * 点击音乐图标跳转
     */
    public void onMusicClick() {
        NavigationUtil.navigateToMusicSelectPage(this);
    }

    /**
     * 点击拍照
     */
    public void onCameraClick() {
    }


    /**
     * 点击图片图标跳转
     */
    public void onImageClick() {
        NavigationUtil.navigateToImageSelectPage(this);
    }

    /**
     * 点击发送消息按钮
     */
    public void onSendClick() {
        Spanned messageContent = mEtMsg.getText();
        String message = Html.toHtml(messageContent);
        //去除转换过程中自动生成的html标签
        int start = message.indexOf(">") + 1;
        int end = message.lastIndexOf("</p>");
        if (end != -1) {
            message = message.substring(start, end);
            Log.d("ChatActivity", message);
            //发送消息
            sendMsg(message);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String musicName = intent.getStringExtra(MSG_MUSIC_NAME);
        String musicPath = intent.getStringExtra(MSG_MUSIC_PATH);
        String imagePath = intent.getStringExtra(MSG_IMAGE_PATH);
        IMMessage message = null;
        if (musicName != null && musicPath != null) {
            File musicFile = new File(musicPath);
            message = new MessageBuilder().createFileMessage(mAccount, SessionTypeEnum.P2P, musicFile, musicName);
            //发送文件附件消息
            NIMClient.getService(MsgService.class).sendMessage(message, true);
        } else if (imagePath != null) {
            File imgFile = new File(imagePath);
            message = MessageBuilder.createImageMessage(mAccount, SessionTypeEnum.P2P, imgFile);
            NIMClient.getService(MsgService.class).sendMessage(message, true);
        }
        if (message != null) {
            mMsgList.add(message);
            mAdapter.notifyDataSetChanged();
            mLvChat.setSelection(mAdapter.getCount() - 1);
            // 监听消息状态变化
            Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
                @Override
                public void onEvent(IMMessage msg) {
                    if (msg.getAttachStatus() == AttachStatusEnum.transferred) {
                        ToastUtil.show(ChatActivity.this, "发送成功");
                    }
                }
            };
            //监听文件附件消息发送状态
            NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(statusObserver, true);
        }
    }


    @Override
    protected void onDestroy() {
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(mIncomingMsgObserver, false);
        super.onDestroy();
    }
}
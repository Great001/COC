package com.example.liaohaicongsx.coc.util;

import android.content.Context;
import android.content.Intent;

import com.example.liaohaicongsx.coc.activity.AddFriendActivity;
import com.example.liaohaicongsx.coc.activity.ChatActivity;
import com.example.liaohaicongsx.coc.activity.EditUserInfoActivity;
import com.example.liaohaicongsx.coc.activity.FindPwdActivity;
import com.example.liaohaicongsx.coc.activity.ImageSelectActivity;
import com.example.liaohaicongsx.coc.activity.LoginActivity;
import com.example.liaohaicongsx.coc.activity.MainActivity;
import com.example.liaohaicongsx.coc.activity.RegisterActivity;
import com.example.liaohaicongsx.coc.activity.SelectMusicActivity;
import com.example.liaohaicongsx.coc.activity.UserInfoActivity;
import com.example.liaohaicongsx.coc.activity.UserSettingActivity;

/**
 * Created by liaohaicongsx on 2017/04/13.
 */
public class NavigationUtil {

    public static void navigateToMainPage(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToLoginPage(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToLoginPage(Context context, String accid, String token) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(LoginActivity.KEY_ACCOUNT, accid);
        intent.putExtra(LoginActivity.KEY_PWD, token);
        context.startActivity(intent);
    }


    public static void navigateToRegisterPage(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToFindPwdActivity(Context context) {
        Intent intent = new Intent(context, FindPwdActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToAddFriendActivity(Context context) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToChatPage(Context context, String account, String nick) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.ACCOUNT, account);
        intent.putExtra(ChatActivity.NICK, nick);
        context.startActivity(intent);
    }

    public static void navigateToMusicSelectPage(Context context) {
        Intent intent = new Intent(context, SelectMusicActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToImageSelectPage(Context context) {
        Intent intent = new Intent(context, ImageSelectActivity.class);
        context.startActivity(intent);
    }


    public static void musicBackToChatPage(Context context, String name, String path) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.MSG_MUSIC_PATH, path);
        intent.putExtra(ChatActivity.MSG_MUSIC_NAME, name);
        context.startActivity(intent);
    }

    public static void imageBackToChatPage(Context context, String imgPath) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.MSG_IMAGE_PATH, imgPath);
        context.startActivity(intent);

    }



    public static void navigateToUserInfoPage(Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToEditUserInfoPage(Context context, String item) {
        Intent intent = new Intent(context, EditUserInfoActivity.class);
        intent.putExtra(EditUserInfoActivity.EDIT_ITEM, item);
        context.startActivity(intent);
    }


    public static void navigateToSettingPage(Context context) {
        Intent intent = new Intent(context, UserSettingActivity.class);
        context.startActivity(intent);
    }

}

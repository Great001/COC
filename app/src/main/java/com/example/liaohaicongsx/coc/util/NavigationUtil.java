package com.example.liaohaicongsx.coc.util;

import android.content.Context;
import android.content.Intent;

import com.example.liaohaicongsx.coc.activity.AddFriendActivity;
import com.example.liaohaicongsx.coc.activity.ChatActivity;
import com.example.liaohaicongsx.coc.activity.FindPwdActivity;
import com.example.liaohaicongsx.coc.activity.LoginActivity;
import com.example.liaohaicongsx.coc.activity.MainActivity;
import com.example.liaohaicongsx.coc.activity.RegisterActivity;

/**
 * Created by liaohaicongsx on 2017/04/13.
 */
public class NavigationUtil {

    public static void navigatoMainPage(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToLoginPage(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToLoginPage(Context context,String accid,String token){
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra(LoginActivity.KEY_ACCOUNT,accid);
        intent.putExtra(LoginActivity.KEY_PWD,token);
        context.startActivity(intent);
    }


    public static void navigateToRegisterPage(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToFindPwdActivity(Context context){
        Intent intent = new Intent(context, FindPwdActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToAddFriendActivity(Context context){
        Intent intent = new Intent(context, AddFriendActivity.class);
        context.startActivity(intent);
    }

    public static void navigateToChatPage(Context context,String account,String nick){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(ChatActivity.ACCOUNT,account);
        intent.putExtra(ChatActivity.NICK,nick);
        context.startActivity(intent);
    }


}

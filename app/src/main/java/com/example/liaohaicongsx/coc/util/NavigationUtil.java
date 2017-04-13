package com.example.liaohaicongsx.coc.util;

import android.content.Context;
import android.content.Intent;

import com.example.liaohaicongsx.coc.activity.MainActivity;

/**
 * Created by liaohaicongsx on 2017/04/13.
 */
public class NavigationUtil {

    public static void navigatoMainPage(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}

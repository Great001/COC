package com.example.liaohaicongsx.coc.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liaohaicongsx on 2017/04/18.
 */
public class ToastUtil {

    public static void show(Context context, String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context,int strResId){
        Toast.makeText(context,strResId,Toast.LENGTH_SHORT).show();
    }

}

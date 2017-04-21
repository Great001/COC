package com.example.liaohaicongsx.coc;

import android.app.Activity;

import java.lang.ref.SoftReference;
import java.util.Stack;

/**
 * Created by liaohaicongsx on 2017/04/21.
 */
public class AppActivityManager {

    private volatile  static AppActivityManager instance;

    private  Stack<SoftReference<Activity>> stack = new Stack<>();

    public static AppActivityManager getInstance(){
        if(instance == null){
            synchronized (AppActivityManager.class){
                instance = new AppActivityManager();
            }
        }
        return instance;
    }

    public  int getStackEntryCount() {
        return stack.size();
    }

    public void push(Activity activity) {
        if(activity != null) {
            SoftReference<Activity> ref = new SoftReference<Activity>(activity);
            stack.push(ref);
        }
    }

    public void remove(Activity activity) {
       for(SoftReference<Activity> ref : stack){
           if(ref != null && ref.get() == activity){
               stack.remove(ref);
           }
       }
    }

    public Activity topActivity(){
        return stack.peek().get();
    }



    public  Activity findActivity(Activity activity) {
        for (SoftReference<Activity> ref : stack) {
            if (ref != null && ref.get() == activity) {
                return ref.get();
            }
        }
        return null;
    }


    public void clear() {
            for (SoftReference<Activity> ref : stack) {
                if (ref != null && ref.get() != null) {
                    ref.get().finish();
                }
                stack.pop();
            }
    }


}

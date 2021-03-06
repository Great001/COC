package com.example.liaohaicongsx.coc;

import android.app.Activity;

import java.lang.ref.SoftReference;
import java.util.Stack;

/**
 * Created by liaohaicongsx on 2017/04/21.
 */
public class AppActivityManager {

    private volatile static AppActivityManager sAppActivityManager;

    private Stack<SoftReference<Activity>> mStack = new Stack<>();

    public static AppActivityManager getAppActivityManager() {
        if (sAppActivityManager == null) {
            synchronized (AppActivityManager.class) {
                sAppActivityManager = new AppActivityManager();
            }
        }
        return sAppActivityManager;
    }

    public int getStackEntryCount() {
        return mStack.size();
    }

    public void push(Activity activity) {
        if (activity != null) {
            SoftReference<Activity> ref = new SoftReference<Activity>(activity);
            mStack.push(ref);
        }
    }

    public void pop() {
        mStack.pop();
    }


    public void remove(Activity activity) {
        for (SoftReference<Activity> ref : mStack) {
            if (ref != null && ref.get() == activity) {
                mStack.remove(ref);
            }
        }
    }

    public Activity topActivity() {
        while (!mStack.isEmpty()) {
            if (mStack.peek() != null && mStack.peek().get() != null) {
                break;
            } else {
                mStack.pop();
            }
        }
        return mStack.peek().get();
    }


    public Activity findActivity(Activity activity) {
        for (SoftReference<Activity> ref : mStack) {
            if (ref != null && ref.get() == activity) {
                return ref.get();
            }
        }
        return null;
    }


    public void clear() {
        while (!mStack.empty()) {
            SoftReference<Activity> ref = mStack.pop();
            if (ref != null && ref.get() != null) {
                ref.get().finish();
            }
        }
    }


}

package com.univoxer.android.user;

import android.content.Context;

import com.univoxer.android.utils.SharedPrefsUtil;
import com.univoxer.android.network.RequestManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gustavo on 18/06/2016.
 * User
 */
public class User {

    private boolean translatorStatus = true;

    private static User mInstance;

    private static Map<String, String> mUserParams;

    private User() {
        mUserParams = new HashMap<>();
    }

    public static synchronized User getInstance() {
        if (mInstance == null) {
            mInstance = new User();
        }
        return mInstance;
    }

    public void saveUserData(Map<String, String> params) {
        mUserParams = params;
    }

    public String getUserData(String key) {
        return mUserParams.get(key);
    }

    public Map<String, String> getAllUserData() {
        return mUserParams;
    }

    public void setUserData(String key, String value) {
        mUserParams.put(key, value);
    }

    public void clearUserData(Context context){
        mUserParams.clear();
        SharedPrefsUtil.clearData(context);
    }

    public boolean isUser() {
        return mUserParams.get(RequestManager.ROLE).equals("1");
    }

    public boolean isTranslator() {
        return mUserParams.get(RequestManager.ROLE).equals("2");
    }
}
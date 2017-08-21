package com.univoxer.android.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Gustavo on 14/06/2016.
 * SharedPrefsUtil
 */
public class SharedPrefsUtil {

    private static final String SHARED_PREF_FILE_KEY = "com.univoxer.android.USERPREFS";

    public static final String USER_LOGIN = "key_user_login";
    public static final String USER_PASSWORD = "key_user_password";

    public static final String USER_CALL_FLAG = "key_user_call_flag";
    public static final String TRANSLATOR_CALL_FLAG = "key_translator_call_flag";

    public static final String TRANSLATOR_STATUS = "key_translator_status";

    public static void clearData(Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.clear();
        editor.apply();
    }

    public static String getUserLogin(Context context) {
        return getSharedPreference(context).getString(USER_LOGIN, null);
    }

    public static void setUserLogin(Context context, String user) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(USER_LOGIN, user);
        editor.apply();
    }

    public static void setUserPassword(Context context, String password) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(USER_PASSWORD, password);
        editor.apply();
    }

    public static String getUserPassword(Context context) {
        return getSharedPreference(context).getString(USER_PASSWORD, null);
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(SHARED_PREF_FILE_KEY, Context.MODE_PRIVATE);
    }

    public static boolean getTranslatorStatus(Context context) {
        return getSharedPreference(context).getBoolean(TRANSLATOR_STATUS, true);
    }

    public static void setTranslatorStatus(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(TRANSLATOR_STATUS, status);
        editor.apply();
    }

    public static int getTranslatorCallFlag(Context context) {
        return getSharedPreference(context).getInt(TRANSLATOR_CALL_FLAG, 2);
    }

    public static void setTranslatorCallFlag(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(TRANSLATOR_CALL_FLAG, id);
        editor.apply();
    }

    public static int getUserCallFlag(Context context) {
        return getSharedPreference(context).getInt(USER_CALL_FLAG, 1);
    }

    public static void setUserCallFlag(Context context, int id) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(USER_CALL_FLAG, id);
        editor.apply();
    }
}

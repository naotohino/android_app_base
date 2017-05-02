package com.example.androidappbase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



import com.example.androidappbase.model.User;
import com.example.androidappbase.util.ApiUtils;




public class AppSharedPrefs {

    private final static String KEY_UID = "uid";
    private final static String KEY_CLIENT = "client";
    private final static String KEY_ACCESS_TOKEN = "access-token";

    private final static String KEY_USER = "user";
    private final static String KEY_EMAIL = "email";
    private final static String KEY_NICKNAME = "nickname";


    public static void setCredentials(Context context, String uid, String client, String accessToken){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences
                .edit()
                .putString(KEY_UID, uid)
                .putString(KEY_CLIENT, client)
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .commit();

    }

    public static String getUid(Context context){
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(KEY_UID,"");
    }
    public static String getClient(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(KEY_CLIENT,"");
    }
    public static String getAccessToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(KEY_ACCESS_TOKEN,"");
    }

    public static void setUser(Context context, User user){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences
                .edit()
                .putString(KEY_EMAIL, user.email)
                .putString(KEY_NICKNAME, user.nickname)
                .commit();
        preferences
                .edit()
                .putString(KEY_USER, ApiUtils.getGson().toJson(user))
                .commit();
    }

    public static User getUser(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String str = preferences.getString(KEY_USER, null);
        if(str == null){
            return null;
        }
        return ApiUtils.getGson().fromJson(str, User.class);
    }

}

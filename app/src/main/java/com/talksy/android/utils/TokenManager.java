package com.talksy.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {

    private static final String PREF_NAME = "TalksyPrefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_FULL_NAME = "user_full_name";
    private static final String KEY_USER_EMAIL = "user_email";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public TokenManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_JWT_TOKEN, token);
        editor.apply();
    }


public String getToken() {
    String token = sharedPreferences.getString(KEY_JWT_TOKEN, null);
    Log.d("TokenManager", "Retrieved token: " + (token != null ? "Token exists" : "Token is null"));
    return token;
}

    public void saveUserInfo(String userId, String fullName, String email) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_FULL_NAME, fullName);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getUserFullName() {
        return sharedPreferences.getString(KEY_USER_FULL_NAME, null);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }
}

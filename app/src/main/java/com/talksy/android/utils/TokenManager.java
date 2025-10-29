package com.talksy.android.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {
    
    private static final String PREFS_NAME = "talksy_secure_prefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_DATA = "user_data";
    
    private static SharedPreferences getEncryptedPrefs(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            
            return EncryptedSharedPreferences.create(
                    context,
                    PREFS_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // Fallback to regular SharedPreferences if encryption fails
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
    }
    
    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply();
    }
    
    public static String getToken(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        return prefs.getString(KEY_JWT_TOKEN, null);
    }
    
    public static void saveUserId(Context context, String userId) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        prefs.edit().putString(KEY_USER_ID, userId).apply();
    }
    
    public static String getUserId(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        return prefs.getString(KEY_USER_ID, null);
    }
    
    public static void saveUserData(Context context, String userData) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        prefs.edit().putString(KEY_USER_DATA, userData).apply();
    }
    
    public static String getUserData(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        return prefs.getString(KEY_USER_DATA, null);
    }
    
    public static void clearAll(Context context) {
        SharedPreferences prefs = getEncryptedPrefs(context);
        prefs.edit().clear().apply();
    }
    
    public static boolean isLoggedIn(Context context) {
        return getToken(context) != null;
    }
}

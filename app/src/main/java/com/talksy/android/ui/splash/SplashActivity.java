package com.talksy.android.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.talksy.android.R;
import com.talksy.android.ui.auth.LoginActivity;
import com.talksy.android.ui.chat.ChatListActivity;
import com.talksy.android.utils.TokenManager;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TokenManager tokenManager = new TokenManager(SplashActivity.this);
                Intent i;
                if (tokenManager.isLoggedIn()) {
                    i = new Intent(SplashActivity.this, ChatListActivity.class);
                } else {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

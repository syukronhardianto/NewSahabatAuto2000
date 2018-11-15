package com.example.bismillah.newsahabatauto2000.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bismillah.newsahabatauto2000.R;
import com.example.bismillah.newsahabatauto2000.SessionManager.SessionManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                Intent toMenuIntent = new Intent(SplashScreen.this, LoginActivity.class);
                SplashScreen.this.startActivity(toMenuIntent);
                SplashScreen.this.finish();
            }
        }, 3000);
    }
}

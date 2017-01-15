package com.datarockets.mnchkn.ui;


import android.content.Intent;
import android.os.Bundle;

import com.datarockets.mnchkn.R;
import com.datarockets.mnchkn.ui.base.BaseActivity;
import com.datarockets.mnchkn.ui.onboard.WelcomeActivity;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

    }

    @Override
    protected void onResume() {
        super.onResume();
        trackWithProperties("Current activity", "Activity name", "SplashActivity");
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

}

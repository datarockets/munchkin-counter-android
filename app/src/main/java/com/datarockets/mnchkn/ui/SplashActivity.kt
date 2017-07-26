package com.datarockets.mnchkn.ui

import android.content.Intent
import android.os.Bundle

import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.ui.base.BaseActivity
import com.datarockets.mnchkn.ui.onboard.WelcomeActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()
        trackWithProperties("Current activity", "Activity name", "SplashActivity")
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

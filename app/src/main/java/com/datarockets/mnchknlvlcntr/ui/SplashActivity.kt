package com.datarockets.mnchknlvlcntr.ui

import android.content.Intent
import android.os.Bundle

import com.datarockets.mnchknlvlcntr.R
import com.datarockets.mnchknlvlcntr.ui.base.BaseActivity
import com.datarockets.mnchknlvlcntr.ui.onboard.WelcomeActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        trackWithProperties("Current activity", "Activity name", "SplashActivity")
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

package com.datarockets.mnchkn.ui.share

import android.content.Intent
import com.datarockets.mnchkn.ui.base.MvpView

interface ShareResultView: MvpView {
    fun launchShareableIntent(generatedIntent: Intent)
}
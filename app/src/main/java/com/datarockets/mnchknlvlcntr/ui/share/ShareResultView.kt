package com.datarockets.mnchknlvlcntr.ui.share

import android.content.Intent
import com.datarockets.mnchknlvlcntr.ui.base.MvpView

interface ShareResultView: MvpView {
    fun launchShareableIntent(generatedIntent: Intent)
}

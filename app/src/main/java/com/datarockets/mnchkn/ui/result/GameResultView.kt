package com.datarockets.mnchkn.ui.result

import android.content.Intent
import com.datarockets.mnchkn.ui.base.MvpView

interface GameResultView : MvpView {
    fun loadChartFragments()
    fun launchShareableIntent(shareableIntent: Intent)
}

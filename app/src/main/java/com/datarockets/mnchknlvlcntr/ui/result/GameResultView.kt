package com.datarockets.mnchknlvlcntr.ui.result

import android.content.Intent
import com.datarockets.mnchknlvlcntr.ui.base.MvpView

interface GameResultView : MvpView {
    fun loadChartFragments()
    fun launchShareableIntent(shareableIntent: Intent)
}

package com.datarockets.mnchknlvlcntr.ui.player

import com.datarockets.mnchknlvlcntr.ui.base.MvpView

interface PlayerView : MvpView {
    fun showPlayerName(playerName: String)
    fun showPlayerScores(levelScore: String, strengthScore: String)
}

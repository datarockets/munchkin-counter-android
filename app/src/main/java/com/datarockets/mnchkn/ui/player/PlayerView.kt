package com.datarockets.mnchkn.ui.player

import com.datarockets.mnchkn.ui.base.MvpView

interface PlayerView : MvpView {
    fun showPlayerName(playerName: String)
    fun showPlayerScores(levelScore: Int, strengthScore: Int)
}

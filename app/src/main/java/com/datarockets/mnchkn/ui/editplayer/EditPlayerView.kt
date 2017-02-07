package com.datarockets.mnchkn.ui.editplayer

import com.datarockets.mnchkn.ui.base.MvpView

interface EditPlayerView : MvpView {
    fun showPlayerInformation(playerName: String, playerColor: String)
    fun hideEditPlayerDialog(playerId: Long, playerName: String)
}

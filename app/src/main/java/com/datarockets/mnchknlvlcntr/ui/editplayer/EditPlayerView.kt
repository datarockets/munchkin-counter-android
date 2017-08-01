package com.datarockets.mnchknlvlcntr.ui.editplayer

import com.datarockets.mnchknlvlcntr.ui.base.MvpView

interface EditPlayerView : MvpView {
    fun showPlayerInformation(playerName: String, playerColor: String)
    fun hideEditPlayerDialog(playerId: Long, playerName: String)
}

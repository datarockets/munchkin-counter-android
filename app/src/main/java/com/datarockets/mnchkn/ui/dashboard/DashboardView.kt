package com.datarockets.mnchkn.ui.dashboard

import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.MvpView


interface DashboardView : MvpView {
    fun finishGame()
    fun setPlayers(players: List<Player>)
    fun showConfirmFinishGameDialog()
    fun showRollDiceDialog()
    fun updatePlayerInformation(player: Player, position: Int)
    fun keepScreenOn(keepActive: Boolean)
}
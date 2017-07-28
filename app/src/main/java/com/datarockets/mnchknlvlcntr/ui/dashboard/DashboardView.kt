package com.datarockets.mnchknlvlcntr.ui.dashboard

import com.datarockets.mnchknlvlcntr.data.models.Player
import com.datarockets.mnchknlvlcntr.ui.base.MvpView

interface DashboardView : MvpView {
    fun finishGame()
    fun setPlayers(players: List<Player>)
    fun showConfirmFinishGameDialog()
    fun showRollDiceDialog()
    fun updatePlayerInformation(player: Player, position: Int)
    fun keepScreenOn(keepActive: Boolean)
}
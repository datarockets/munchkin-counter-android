package com.datarockets.mnchkn.ui.players


import com.datarockets.mnchkn.models.Player
import com.datarockets.mnchkn.ui.base.MvpView

interface PlayersListView : MvpView {
    fun addPlayerToList(player: Player)
    fun deletePlayerFromList(position: Int)
    fun setPlayersList(players: List<Player>)
    fun showAddNewPlayerDialog()
    fun launchDashboard()
    fun showStartContinueDialog()
    fun showWarning()
}

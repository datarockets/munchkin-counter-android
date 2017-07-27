package com.datarockets.mnchkn.ui.players

import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.MvpView

interface PlayersListView : MvpView {
    fun addPlayerToList(player: Player)
    fun deletePlayerFromList(playerId: Long)
    fun setPlayersList(players: List<Player>)
    fun showAddNewPlayerDialog()
    fun launchDashboard()
    fun showStartContinueDialog()
    fun showWarning()
    fun showShowcase()
}

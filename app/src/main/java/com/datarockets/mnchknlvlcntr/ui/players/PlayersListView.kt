package com.datarockets.mnchknlvlcntr.ui.players

import com.datarockets.mnchknlvlcntr.data.models.Player
import com.datarockets.mnchknlvlcntr.ui.base.MvpView

interface PlayersListView : MvpView {
    fun addPlayerToList(player: Player)
    fun deletePlayerFromList(playerId: Long)
    fun setPlayersList(players: List<Player>)
    fun showAddNewPlayerDialog()
    fun launchDashboard()
    fun showStartContinueDialog()
    fun showWarning()
    fun showShowcase()
    fun showShowcaseCanceledMessage()
}

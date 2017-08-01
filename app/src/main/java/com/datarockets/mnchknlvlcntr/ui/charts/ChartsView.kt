package com.datarockets.mnchknlvlcntr.ui.charts

import com.datarockets.mnchknlvlcntr.data.models.Player
import com.datarockets.mnchknlvlcntr.ui.base.MvpView
import com.github.mikephil.charting.data.LineData

interface ChartsView : MvpView {
    fun showPlayersChart(lineData: LineData)
    fun showPlayersList(players: List<Player>)
}

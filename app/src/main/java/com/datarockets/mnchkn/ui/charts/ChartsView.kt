package com.datarockets.mnchkn.ui.charts

import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.MvpView
import com.github.mikephil.charting.data.LineData

interface ChartsView : MvpView {
    fun showPlayersChart(lineData: LineData)
    fun showPlayersList(players: List<Player>)
}

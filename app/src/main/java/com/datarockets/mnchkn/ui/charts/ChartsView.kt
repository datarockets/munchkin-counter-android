package com.datarockets.mnchkn.ui.charts

import com.datarockets.mnchkn.models.Player
import com.datarockets.mnchkn.ui.base.MvpView
import com.github.mikephil.charting.data.LineDataSet

interface ChartsView : MvpView {
    fun showLineDataSets(lineDataSets: List<LineDataSet>)
    fun showPlayersList(players: List<Player>)
}

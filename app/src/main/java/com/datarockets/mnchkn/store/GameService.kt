package com.datarockets.mnchkn.store

import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player

interface GameService {
    fun insertStep(player: Player)
    fun clearSteps()
    val isGameStarted: Boolean
    fun createPlayerIdGameStepsMap()
    fun setGameStatus(gameStatus: Boolean)
    val scoresChartData: Map<Player, List<GameStep>>
}

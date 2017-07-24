package com.datarockets.mnchkn.data

import android.content.Intent
import android.graphics.Color
import com.datarockets.mnchkn.data.local.DatabaseHelper
import com.datarockets.mnchkn.data.local.PreferencesHelper
import com.datarockets.mnchkn.data.local.SharingHelper
import com.datarockets.mnchkn.data.models.GameStep
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.utils.ColorUtil
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class DataManager
@Inject constructor(private val databaseHelper: DatabaseHelper,
                    private val preferencesHelper: PreferencesHelper,
                    private val sharingHelper: SharingHelper) {

    val localPreferencesHelper = preferencesHelper

    fun getPlayer(playerId: Long): io.reactivex.Observable<Player> {
        return databaseHelper.getPlayer(playerId)
    }

    open fun getPlayers(): Single<List<Player>> {
        return databaseHelper.getPlayers().toList()
    }

    open fun getPlayingPlayers(): Single<List<Player>> {
        return databaseHelper.getPlayingPlayers().toList()
    }

    open fun getPlayers(sortType: Int): Single<List<Player>> {
        when (sortType) {
            0 -> return databaseHelper.getPlayedPlayersByLevel().toList()
            1 -> return databaseHelper.getPlayedPlayersByStrength().toList()
            2 -> return databaseHelper.getPlayedPlayersByTotal().toList()
        }
        return Single.just(null)
    }

    fun addPlayer(playerName: String, position: Int): Observable<Player> {
        val player = Player()
        player.name = playerName
        player.levelScore = 1
        player.strengthScore = 1
        player.color = ColorUtil.generatePlayerAvatarColor()
        player.position = position
        return databaseHelper.setPlayer(player)
    }

    fun changePlayerPosition(movedPlayerId: Long,
                             newPosition: Int): Observable<Void> {
        return databaseHelper.changePlayersPositions(movedPlayerId, newPosition)
    }

    fun setPlayerPlaying(playerId: Long, isPlaying: Boolean): Observable<Void> {
        return databaseHelper.markPlayerPlaying(playerId, isPlaying)
    }

    fun updatePlayerName(playerId: Long, playerName: String): Observable<Void> {
        return databaseHelper.updatePlayerName(playerId, playerName)
    }

    fun updatePlayerScores(playerId: Long, levelScore: Int, strengthScore: Int): Observable<Void> {
        return databaseHelper.updatePlayerScores(playerId, levelScore, strengthScore)
    }

    fun deletePlayer(playerId: Long): Observable<Void> {
        return databaseHelper.deletePlayer(playerId)
    }

    fun clearGameSteps(): Observable<Void> {
        return databaseHelper.clearGameSteps()
    }

    fun getLineData(type: Int): Observable<LineData> {
        return Observable.create { subscriber ->
            val playersList = databaseHelper.getPlayingPlayers()
            val gameSteps = databaseHelper.getGameSteps()
            val playerGameSteps = mutableMapOf<Player, List<GameStep>>()

            playersList.forEach { player ->
                val playerSteps = mutableListOf<GameStep>()
                gameSteps.forEach { step ->
                    if (step.playerId == player.id) {
                        playerSteps.add(step)
                    }
                }
                playerGameSteps.put(player, playerSteps)
            }

            var playerLines = ArrayList<ILineDataSet>()
            val playerColors = mutableListOf<String>()

            playerGameSteps.keys.forEach { player ->
                val color = player.color
                playerColors.add(color!!)
            }

            playerGameSteps.values.forEachIndexed { index, playerStepsList ->
                val entries = mutableListOf<Entry>()
                playerStepsList.forEachIndexed { index, step ->
                    when (type) {
                        0 -> {
                            val playerLevel = step.playerLevel
                            entries.add(Entry(index.toFloat(), playerLevel.toFloat()))
                        }
                        1 -> {
                            val playerStrength = step.playerStrength
                            entries.add(Entry(index.toFloat(), playerStrength.toFloat()))
                        }
                        2 -> {
                            val playerTotal = step.playerLevel + step.playerStrength
                            entries.add(Entry(index.toFloat(), playerTotal.toFloat()))
                        }
                    }
                }


                val lineDataSet = LineDataSet(entries, "")
                lineDataSet.setDrawCircles(false)
                lineDataSet.color = Color.parseColor(playerColors[index])
                lineDataSet.lineWidth = 3f
                lineDataSet.cubicIntensity = 0.1f
                lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
                lineDataSet.setDrawValues(false)
                lineDataSet.isHighlightEnabled = false
                playerLines.add(lineDataSet)
            }

            val lineData = LineData(playerLines)
            subscriber.onNext(lineData)
            subscriber.onComplete()
        }
    }

    fun addGameStep(playerId: Long, levelScore: Int, strengthScore: Int): Observable<Void> {
        val gameStep = GameStep()
        gameStep.playerId = playerId
        gameStep.playerLevel = levelScore
        gameStep.playerStrength = strengthScore
        return databaseHelper.setGameStep(gameStep)
    }

    fun generateShareableIntent(): Observable<Intent> {
        return sharingHelper.generateShareableIntent()
    }

    fun updatePlayersPosition() {
        databaseHelper.updatePlayersPositions().subscribe()
    }
}

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
import rx.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class DataManager
@Inject constructor(private val mDatabaseHelper: DatabaseHelper,
                    private val mPreferencesHelper: PreferencesHelper,
                    private val mSharingHelper: SharingHelper) {

    val preferencesHelper = mPreferencesHelper

    fun getPlayer(playerId: Long): Observable<Player> {
        return mDatabaseHelper.getPlayer(playerId)
    }

    open fun getPlayers(): Observable<List<Player>> {
        return mDatabaseHelper.getPlayers().toList()
    }

    open fun getPlayingPlayers(): Observable<List<Player>> {
        return mDatabaseHelper.getPlayingPlayers().toList()
    }

    open fun getPlayers(sortType: Int): Observable<List<Player>> {
        when (sortType) {
            0 -> return mDatabaseHelper.getPlayedPlayersByLevel().toList()
            1 -> return mDatabaseHelper.getPlayedPlayersByStrength().toList()
            2 -> return mDatabaseHelper.getPlayedPlayersByTotal().toList()
        }
        return Observable.just(null)
    }

    fun addPlayer(playerName: String, position: Int): Observable<Player> {
        val player = Player()
        player.name = playerName
        player.levelScore = 1
        player.strengthScore = 1
        player.color = ColorUtil.generatePlayerAvatarColor()
        player.position = position
        return mDatabaseHelper.setPlayer(player)
    }

    fun changePlayerPosition(movedPlayerId: Long,
                             newPosition: Int): Observable<Void> {
        return mDatabaseHelper.changePlayersPositions(movedPlayerId, newPosition)
    }

    fun setPlayerPlaying(playerId: Long, isPlaying: Boolean): Observable<Void> {
        return mDatabaseHelper.markPlayerPlaying(playerId, isPlaying)
    }

    fun updatePlayerName(playerId: Long, playerName: String): Observable<Void> {
        return mDatabaseHelper.updatePlayerName(playerId, playerName)
    }

    fun updatePlayerScores(playerId: Long, levelScore: Int, strengthScore: Int): Observable<Void> {
        return mDatabaseHelper.updatePlayerScores(playerId, levelScore, strengthScore)
    }

    fun deletePlayer(playerId: Long): Observable<Void> {
        return mDatabaseHelper.deletePlayer(playerId)
    }

    fun clearGameSteps(): Observable<Void> {
        return mDatabaseHelper.clearGameSteps()
    }

    fun getLineData(type: Int): Observable<LineData> {
        return Observable.create { subscriber ->
            val playersList = mDatabaseHelper.getPlayingPlayers()
            val gameSteps = mDatabaseHelper.getGameSteps()
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
            subscriber.onCompleted()
        }
    }

    fun addGameStep(playerId: Long, levelScore: Int, strengthScore: Int): Observable<Void> {
        val gameStep = GameStep()
        gameStep.playerId = playerId
        gameStep.playerLevel = levelScore
        gameStep.playerStrength = strengthScore
        return mDatabaseHelper.setGameStep(gameStep)
    }

    fun generateShareableIntent(): Observable<Intent> {
        return mSharingHelper.generateShareableIntent()
    }

    fun updatePlayersPosition() {
        mDatabaseHelper.updatePlayersPositions().subscribe()
    }

}

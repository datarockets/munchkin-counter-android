package com.datarockets.mnchknlvlcntr.ui.player

import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlayerPresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<PlayerView> {

    private var playerView: PlayerView? = null
    private var disposable: Disposable? = null
    private var playerId: Long = 0
    private var levelScore: Int = 0
    private var strengthScore: Int = 0

    override fun attachView(mvpView: PlayerView) {
        playerView = mvpView
    }

    fun loadPlayerScores(playerId: Long) {
        disposable = dataManager.getPlayer(playerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player ->
                    this.playerId = player.id
                    levelScore = player.levelScore
                    strengthScore = player.strengthScore
                    playerView?.showPlayerName(player.name!!)
                    playerView?.showPlayerScores(levelScore.toString(), strengthScore.toString())
                }
    }

    fun increaseLevelScore() {
        levelScore += 1
        updatePlayerScores()
    }

    fun decreaseLevelScore() {
        levelScore -= 1
        updatePlayerScores()
    }

    fun increaseStrengthScore() {
        strengthScore += 1
        updatePlayerScores()
    }

    fun decreaseStrengthScore() {
        strengthScore -= 1
        updatePlayerScores()
    }

    fun updatePlayerScores() {
        disposable = dataManager.updatePlayerScores(playerId, levelScore, strengthScore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    playerView?.showPlayerScores(levelScore.toString(), strengthScore.toString())
                }
                .subscribe()
    }

    override fun detachView() {
        playerView = null
        disposable?.dispose()
    }
}

package com.datarockets.mnchkn.ui.player

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class PlayerPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<PlayerView> {

    private var mPlayerView: PlayerView? = null
    private var mSubscription: Subscription? = null
    private var mPlayerId: Long = 0
    private var mLevelScore: Int = 0
    private var mStrengthScore: Int = 0

    override fun attachView(mvpView: PlayerView) {
        mPlayerView = mvpView
    }

    fun loadPlayerScores(playerId: Long) {
        mSubscription = mDataManager.getPlayer(playerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player ->
                    mPlayerId = player.id
                    mLevelScore = player.levelScore
                    mStrengthScore = player.strengthScore
                    mPlayerView?.showPlayerName(player?.name!!)
                    mPlayerView?.showPlayerScores(mLevelScore.toString(), mStrengthScore.toString())
                }
    }

    fun increaseLevelScore() {
        mLevelScore += 1
        updatePlayerScores()
    }

    fun decreaseLevelScore() {
        mLevelScore -= 1
        updatePlayerScores()
    }

    fun increaseStrengthScore() {
        mStrengthScore += 1
        updatePlayerScores()
    }

    fun decreaseStrengthScore() {
        mStrengthScore -= 1
        updatePlayerScores()
    }

    fun updatePlayerScores() {
        mSubscription = mDataManager.updatePlayerScores(mPlayerId, mLevelScore, mStrengthScore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted { mPlayerView?.showPlayerScores(
                        mLevelScore.toString(),
                        mStrengthScore.toString())
                }
                .subscribe()
    }

    override fun detachView() {
        mPlayerView = null
        mSubscription?.unsubscribe()
    }

}

package com.datarockets.mnchkn.ui.player


import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class PlayerPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<PlayerView> {

    private var mPlayerView: PlayerView? = null
    private var mSubscription: Subscription? = null
    private var mSelectedPlayer: Player? = null

    override fun attachView(mvpView: PlayerView) {
        mPlayerView = mvpView
    }

    fun loadPlayerScores(playerId: Int) {
        mSubscription = mDataManager.getPlayer(playerId.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player ->
                    mSelectedPlayer = player
                    val playerName = mSelectedPlayer?.name!!
                    val levelScore = mSelectedPlayer?.levelScore
                    val strengthScore = mSelectedPlayer?.strengthScore
                    mPlayerView?.showPlayerName(playerName)
                    mPlayerView?.showPlayerScores(levelScore!!, strengthScore!!)
                }
    }

    fun increaseLevelScore() {
        mSelectedPlayer?.levelScore!! + 1
    }

    fun decreaseLevelScore() {
        mSelectedPlayer?.levelScore!! - 1
    }

    fun increaseStrengthScore() {
        mSelectedPlayer?.strengthScore!! + 1
    }

    fun decreaseStrengthScore() {
        mSelectedPlayer?.strengthScore!! - 1
    }

    override fun detachView() {
        mPlayerView = null
        mSubscription?.unsubscribe()
    }

}

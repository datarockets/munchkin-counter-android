package com.datarockets.mnchkn.ui.player


import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.models.Player
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class PlayerPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<PlayerView> {

    private var mPlayerView: PlayerView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: PlayerView) {
        mPlayerView = mvpView
    }

    fun loadPlayerScores(playerId: Int) {
        mSubscription = mDataManager.getPlayer(playerId.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Player>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(player: Player) {
                        val playerName = player.name!!
                        val levelScore = player.levelScore!!
                        val strengthScore = player.strengthScore
                        mPlayerView?.showPlayerName(playerName)
                        mPlayerView?.showPlayerScores(levelScore, strengthScore)
                    }
                })
    }

    fun increaseLevelScore() {}

    fun decreaseLevelScore() {

    }

    fun increaseStrengthScore() {

    }

    fun decreaseStrengthScore() {

    }

    override fun detachView() {
        mPlayerView = null
        mSubscription?.unsubscribe()
    }

}

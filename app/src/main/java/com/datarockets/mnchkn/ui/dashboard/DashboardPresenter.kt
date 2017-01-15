package com.datarockets.mnchkn.ui.dashboard

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.models.Player
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class DashboardPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<DashboardView> {

    private var mDashboardView: DashboardView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: DashboardView) {
        mDashboardView = mvpView
    }

    fun checkIsScreenShouldBeOn() {
        val isScreenShouldBeOn = mDataManager.preferencesHelper.isWakeLockActive
        mDashboardView?.keepScreenOn(isScreenShouldBeOn)
    }

    fun getPlayers() {
        mSubscription = mDataManager.players
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<Player>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(players: List<Player>) {
                        mDashboardView?.setPlayers(players)
                    }
                })
    }

    fun updatePlayerInformation(player: Player, position: Int) {
        mSubscription = mDataManager.updatePlayer(player)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Player>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(player: Player) {
                        mDashboardView?.updatePlayerInformation(player, position)
                    }
                })
    }

    fun insertStep(player: Player) {
        mSubscription = mDataManager.addGameStep(player)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Void>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(aVoid: Void) {

                    }
                })
    }

    fun setGameFinished() {
        mDataManager.preferencesHelper.setGameStatus(false)
    }

    override fun detachView() {
        mDashboardView = null
        mSubscription?.unsubscribe()
    }

}

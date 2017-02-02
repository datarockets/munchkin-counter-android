package com.datarockets.mnchkn.ui.dashboard

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.Presenter
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

    fun getPlayingPlayers() {
        mSubscription = mDataManager.getPlayingPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players -> mDashboardView?.setPlayers(players) }
    }

    fun updatePlayerInformation(player: Player, position: Int) {
//        mSubscription = mDataManager.updatePlayer(player)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { player -> mDashboardView?.updatePlayerInformation(player, position) }
    }

    fun insertStep(player: Player) {
        mSubscription = mDataManager.addGameStep(player)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                }
    }

    fun setGameFinished() {
        mDataManager.preferencesHelper.setGameStatus(false)
    }

    override fun detachView() {
        mDashboardView = null
        mSubscription?.unsubscribe()
    }

}

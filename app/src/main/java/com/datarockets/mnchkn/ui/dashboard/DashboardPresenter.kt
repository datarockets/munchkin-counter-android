package com.datarockets.mnchkn.ui.dashboard

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DashboardPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<DashboardView> {

    private var dashboardView: DashboardView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: DashboardView) {
        dashboardView = mvpView
    }

    fun checkIsScreenShouldBeOn() {
        val isScreenShouldBeOn = mDataManager.localPreferencesHelper.isWakeLockActive
        dashboardView?.keepScreenOn(isScreenShouldBeOn)
    }

    fun getPlayingPlayers() {
        disposable = mDataManager.getPlayingPlayers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players ->
                    dashboardView?.setPlayers(players)
                    players.forEach { insertStep(it.id, it.levelScore, it.strengthScore) }
                }
    }

    fun insertStep(playerId: Long, levelScore: Int, strengthScore: Int) {
        disposable = mDataManager.addGameStep(playerId, levelScore, strengthScore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun setGameFinished() {
        mDataManager.localPreferencesHelper.setGameStatus(false)
    }

    override fun detachView() {
        dashboardView = null
        disposable?.dispose()
    }
}

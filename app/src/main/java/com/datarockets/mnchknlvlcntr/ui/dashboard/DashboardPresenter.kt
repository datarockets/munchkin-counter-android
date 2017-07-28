package com.datarockets.mnchknlvlcntr.ui.dashboard

import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.data.utils.SortType
import com.datarockets.mnchknlvlcntr.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DashboardPresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<DashboardView> {

    private var dashboardView: DashboardView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: DashboardView) {
        dashboardView = mvpView
    }

    fun checkIsScreenShouldBeOn() {
        val isScreenShouldBeOn = dataManager.localPreferencesHelper.isWakeLockActive
        dashboardView?.keepScreenOn(isScreenShouldBeOn)
    }

    fun getPlayingPlayers() {
        disposable = dataManager.getPlayers(SortType.POSITION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players ->
                    dashboardView?.setPlayers(players)
                    players.forEach { insertStep(it.id, it.levelScore, it.strengthScore) }
                }
    }

    fun insertStep(playerId: Long, levelScore: Int, strengthScore: Int) {
        disposable = dataManager.addGameStep(playerId, levelScore, strengthScore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun setGameFinished() {
        dataManager.localPreferencesHelper.setGameStatus(false)
    }

    override fun detachView() {
        dashboardView = null
        disposable?.dispose()
    }
}

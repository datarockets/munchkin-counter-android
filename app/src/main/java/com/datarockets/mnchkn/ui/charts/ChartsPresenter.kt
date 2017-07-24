package com.datarockets.mnchkn.ui.charts

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChartsPresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<ChartsView> {

    private var chartsView: ChartsView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: ChartsView) {
        chartsView = mvpView
    }

    fun loadChartData(type: Int) {
        disposable = dataManager.getLineData(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { lineData -> chartsView?.showPlayersChart(lineData) }
    }

    fun loadPlayers(sortType: Int) {
        disposable = dataManager.getPlayers(sortType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players -> chartsView?.showPlayersList(players) }
    }

    override fun detachView() {
        chartsView = null
        disposable?.dispose()
    }
}

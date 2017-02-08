package com.datarockets.mnchkn.ui.charts

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class ChartsPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<ChartsView> {

    private var mChartsView: ChartsView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: ChartsView) {
        mChartsView = mvpView
    }

    fun loadChartData(type: Int) {
        mSubscription = mDataManager.getLineData(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { lineData -> mChartsView?.showPlayersChart(lineData) }
    }

    fun loadPlayers(sortType: Int) {
        mSubscription = mDataManager.getPlayers(sortType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players -> mChartsView?.showPlayersList(players) }
    }

    override fun detachView() {
        mChartsView = null
        mSubscription?.unsubscribe()
    }


}

package com.datarockets.mnchkn.ui.charts

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.models.Player
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscriber
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

    }

    fun loadPlayers(orderBy: Int) {
        mSubscription = mDataManager.getPlayers(orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<Player>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onNext(players: List<Player>) {
                        mChartsView?.showPlayersList(players)
                    }
                })
    }

    override fun detachView() {
        mChartsView = null
        mSubscription?.unsubscribe()
    }


}

package com.datarockets.mnchkn.ui.result


import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class GameResultPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<GameResultView> {

    private var mGameResultView: GameResultView? = null
    private val mSubscriptions: CompositeSubscription = CompositeSubscription()

    override fun attachView(mvpView: GameResultView) {
        mGameResultView = mvpView
    }

    fun generateShareResultLink() {
        mSubscriptions.add(mDataManager.generateShareableIntent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { intent ->
                    mGameResultView?.launchShareableIntent(intent)
                })
    }

    fun clearGameResults() {
        mSubscriptions.add(mDataManager.clearGameSteps().subscribe())
        mSubscriptions.add(mDataManager.clearPlayerStats().subscribe())
    }

    override fun detachView() {
        mGameResultView = null
        mSubscriptions.unsubscribe()
    }

}

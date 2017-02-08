package com.datarockets.mnchkn.ui.result


import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class GameResultPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<GameResultView> {

    private var mGameResultView: GameResultView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: GameResultView) {
        mGameResultView = mvpView
    }

    fun generateShareResultLink() {
        mSubscription = mDataManager.generateShareableIntent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { intent ->
                    mGameResultView?.launchShareableIntent(intent)
                }
    }

    fun clearGameResults() {
        mSubscription = mDataManager.clearGameSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }


    override fun detachView() {
        mGameResultView = null
        mSubscription?.unsubscribe()
    }

}

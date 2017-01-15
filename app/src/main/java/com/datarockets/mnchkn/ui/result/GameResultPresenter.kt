package com.datarockets.mnchkn.ui.result


import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import javax.inject.Inject

class GameResultPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<GameResultView> {

    private var mGameResultView: GameResultView? = null
    private val mSubscription: Subscription? = null

    override fun attachView(mvpView: GameResultView) {
        mGameResultView = mvpView
    }

    fun onBackPressed() {}

    fun onStop() {}

    override fun detachView() {
        mGameResultView = null
        mSubscription?.unsubscribe()
    }

}

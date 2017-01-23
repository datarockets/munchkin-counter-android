package com.datarockets.mnchkn.ui.onboard

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import javax.inject.Inject

class WelcomePresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<WelcomeView> {

    private var mWelcomeView: WelcomeView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: WelcomeView) {
        mWelcomeView = mvpView
    }

    fun checkIsOnboardingSeen() {
        if (mDataManager.preferencesHelper.checkIsUserSeenOnboarding()) {
            mWelcomeView?.openPlayersActivity()
        }
    }

    fun setOnboardingSeen() {
        mDataManager.preferencesHelper.setOnboardingSeen()
    }

    override fun detachView() {
        mWelcomeView = null
        mSubscription?.unsubscribe()
    }

}
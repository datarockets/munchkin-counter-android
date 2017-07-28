package com.datarockets.mnchknlvlcntr.ui.onboard

import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.ui.base.Presenter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class WelcomePresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<WelcomeView> {

    private var welcomeView: WelcomeView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: WelcomeView) {
        welcomeView = mvpView
    }

    fun checkIsOnboardingSeen() {
        if (dataManager.localPreferencesHelper.checkIsUserSeenOnboarding()) {
            welcomeView?.openPlayersActivity()
        }
    }

    fun setOnboardingSeen() {
        dataManager.localPreferencesHelper.setOnboardingSeen()
    }

    override fun detachView() {
        welcomeView = null
        disposable?.dispose()
    }
}
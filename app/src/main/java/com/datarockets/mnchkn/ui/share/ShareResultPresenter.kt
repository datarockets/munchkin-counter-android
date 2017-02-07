package com.datarockets.mnchkn.ui.share

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.data.local.SharingHelper
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


class ShareResultPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<ShareResultView> {

    private var mShareResultView: ShareResultView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: ShareResultView) {
        mShareResultView = mvpView
    }

    fun shareGameResults(sharingOption: SharingHelper.SHARE_WITH) {
        mSubscription = mDataManager.generateShareableIntent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { shareableIntent ->
                    mShareResultView?.launchShareableIntent(shareableIntent)
                }
    }

    override fun detachView() {
        mShareResultView = null
        mSubscription?.unsubscribe()
    }

}
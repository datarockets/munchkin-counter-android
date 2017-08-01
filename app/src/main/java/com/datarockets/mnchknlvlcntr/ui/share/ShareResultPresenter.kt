package com.datarockets.mnchknlvlcntr.ui.share

import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.data.local.SharingHelper
import com.datarockets.mnchknlvlcntr.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShareResultPresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<ShareResultView> {

    private var shareResultView: ShareResultView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: ShareResultView) {
        shareResultView = mvpView
    }

    fun shareGameResults(sharingOption: SharingHelper.SHARE_WITH) {
        disposable = dataManager.generateShareableIntent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { shareableIntent ->
                    shareResultView?.launchShareableIntent(shareableIntent)
                }
    }

    override fun detachView() {
        shareResultView = null
        disposable?.dispose()
    }
}
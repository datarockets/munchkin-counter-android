package com.datarockets.mnchkn.ui.share

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.data.local.SharingHelper
import com.datarockets.mnchkn.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShareResultPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<ShareResultView> {

    private var shareResultView: ShareResultView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: ShareResultView) {
        shareResultView = mvpView
    }

    fun shareGameResults(sharingOption: SharingHelper.SHARE_WITH) {
        disposable = mDataManager.generateShareableIntent()
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
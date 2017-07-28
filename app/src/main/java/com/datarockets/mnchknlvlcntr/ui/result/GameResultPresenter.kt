package com.datarockets.mnchknlvlcntr.ui.result

import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameResultPresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<GameResultView> {

    private var gameResultView: GameResultView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: GameResultView) {
        gameResultView = mvpView
    }

    fun generateShareResultLink() {
        disposable = dataManager.generateShareableIntent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { intent ->
                    gameResultView?.launchShareableIntent(intent)
                }
    }

    fun clearGameResults() {
        disposable = dataManager.clearGameSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }


    override fun detachView() {
        gameResultView = null
        disposable?.dispose()
    }
}

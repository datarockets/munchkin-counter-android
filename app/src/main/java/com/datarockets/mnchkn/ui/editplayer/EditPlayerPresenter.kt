package com.datarockets.mnchkn.ui.editplayer

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class EditPlayerPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<EditPlayerView> {

    private var mEditPlayerView: EditPlayerView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: EditPlayerView) {
        mEditPlayerView = mvpView
    }

    fun loadPlayer(playerId: Long) {
        mSubscription = mDataManager.getPlayer(playerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player ->
                    mEditPlayerView?.showPlayerInformation(player.name!!, player.color!!)
                }
    }

    fun updatePlayerName(playerId: Long, playerName: String) {
        mSubscription = mDataManager.updatePlayerName(playerId, playerName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted {
                    mEditPlayerView?.hideEditPlayerDialog(playerId, playerName)
                }
                .subscribe()
    }

    override fun detachView() {
        mEditPlayerView = null
        mSubscription?.unsubscribe()
    }

}

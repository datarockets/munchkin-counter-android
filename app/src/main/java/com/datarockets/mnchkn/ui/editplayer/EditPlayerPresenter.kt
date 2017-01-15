package com.datarockets.mnchkn.ui.editplayer

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.models.Player
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class EditPlayerPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<EditPlayerView> {

    private var mEditPlayerView: EditPlayerView? = null
    private var mSubscription: Subscription? = null
    private lateinit var mPlayer: Player

    override fun attachView(mvpView: EditPlayerView) {
        mEditPlayerView = mvpView
    }

    fun loadPlayer(playerId: Long) {
        mSubscription = mDataManager.getPlayer(playerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player ->
                    mPlayer = player
                    mEditPlayerView?.showPlayerInformation(mPlayer.name!!, mPlayer.color!!)
                }
    }

    fun updatePlayerName(playerName: String) {
        mPlayer?.name = playerName
    }

    fun updatePlayerColor() {

    }

    fun updatePlayer() {
        mSubscription = mDataManager.updatePlayer(mPlayer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{}
    }

    override fun detachView() {
        mEditPlayerView = null
        if (mSubscription != null) mSubscription!!.unsubscribe()
    }

}

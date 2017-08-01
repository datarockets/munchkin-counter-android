package com.datarockets.mnchknlvlcntr.ui.editplayer

import com.datarockets.mnchknlvlcntr.data.DataManager
import com.datarockets.mnchknlvlcntr.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class EditPlayerPresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<EditPlayerView> {

    private var editPlayerView: EditPlayerView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: EditPlayerView) {
        editPlayerView = mvpView
    }

    fun loadPlayer(playerId: Long) {
        disposable = dataManager.getPlayer(playerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player ->
                    editPlayerView?.showPlayerInformation(player.name!!, player.color!!)
                }
    }

    fun updatePlayerName(playerId: Long, playerName: String) {
        disposable = dataManager.updatePlayerName(playerId, playerName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    editPlayerView?.hideEditPlayerDialog(playerId, playerName)
                }
                .subscribe()
    }

    override fun detachView() {
        editPlayerView = null
        disposable?.dispose()
    }
}

package com.datarockets.mnchkn.ui.players

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.ui.base.Presenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class PlayersListPresenter
@Inject constructor(private val mDataManager: DataManager) : Presenter<PlayersListView> {

    private var mPlayersListView: PlayersListView? = null
    private var mSubscription: Subscription? = null

    override fun attachView(mvpView: PlayersListView) {
        mPlayersListView = mvpView
    }

    fun checkIsEnoughPlayers() {
        mSubscription = mDataManager.players
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players ->
                    if (players.size >= 2) {
                        mPlayersListView?.launchDashboard()
                    } else {
                        mPlayersListView?.showWarning()
                    }
                }
    }

    fun addPlayer(playerName: String) {
        mSubscription = mDataManager.addPlayer(playerName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player -> mPlayersListView?.addPlayerToList(player) }
    }

    fun deletePlayerListItem(position: Int, playerId: Long) {
        mSubscription = mDataManager.deletePlayer(playerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {}, {
                    mPlayersListView?.deletePlayerFromList(position)
                })
    }

    fun clearPlayersStats() {
        mSubscription = mDataManager.clearPlayerStats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {}
    }

    fun clearGameSteps() {
        mSubscription = mDataManager.clearGameSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {}
    }

    fun setGameStarted() {
        mDataManager.preferencesHelper.setGameStatus(true)
    }

    fun setGameFinished() {
        mDataManager.preferencesHelper.setGameStatus(false)
    }

    fun checkIsGameStarted() {
        val isGameStarted = mDataManager.preferencesHelper.isGameStarted
        if (isGameStarted) mPlayersListView?.showStartContinueDialog()
    }

    fun getPlayersList() {
        mSubscription = mDataManager.players
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players -> mPlayersListView?.setPlayersList(players) }
    }

    override fun detachView() {
        mPlayersListView = null
        mSubscription?.unsubscribe()
    }

}

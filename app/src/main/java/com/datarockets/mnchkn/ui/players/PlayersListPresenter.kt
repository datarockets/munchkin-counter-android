package com.datarockets.mnchkn.ui.players

import com.datarockets.mnchkn.data.DataManager
import com.datarockets.mnchkn.data.utils.SortType
import com.datarockets.mnchkn.ui.base.Presenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PlayersListPresenter
@Inject constructor(private val dataManager: DataManager) : Presenter<PlayersListView> {

    private var playersListView: PlayersListView? = null
    private var disposable: Disposable? = null

    override fun attachView(mvpView: PlayersListView) {
        playersListView = mvpView
    }

    fun checkIsFirstLaunch() {
        val isFirstLaunch = dataManager.localPreferencesHelper.isFirstLaunch
        if (isFirstLaunch) {
            playersListView?.showShowcase()
        }
    }

    fun checkIsEnoughPlayers() {
        disposable = dataManager.getPlayers(SortType.POSITION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players ->
                    if (players.size >= 2) {
                        playersListView?.launchDashboard()
                    } else {
                        playersListView?.showWarning()
                    }
                }
    }

    fun addPlayer(playerName: String, position: Int) {
        disposable = dataManager.addPlayer(playerName, position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player -> playersListView?.addPlayerToList(player) }
    }

    fun createTempPlayer(playerName: String, position: Int) {
        disposable = dataManager.addTempPlayer(playerName, position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { player -> playersListView?.addPlayerToList(player) }
    }

    fun changePlayerPosition(movedPlayerId: Long, newPosition: Int) {
        disposable = dataManager.changePlayerPosition(movedPlayerId, newPosition)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun markPlayerAsPlaying(playerId: Long, isPlaying: Boolean) {
        disposable = dataManager.setPlayerPlaying(playerId, isPlaying)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun deletePlayerListItem(playerId: Long) {
        disposable = dataManager.deletePlayer(playerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { playersListView?.deletePlayerFromList(playerId) }
                .subscribe()
    }

    fun removeTempPlayer() {
        dataManager.localPreferencesHelper.setFirstLaunch(false)
        deletePlayerListItem(-1)
    }

    fun clearGameSteps() {
        disposable = dataManager.clearGameSteps()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun setGameStarted() {
        dataManager.localPreferencesHelper.setGameStatus(true)
    }

    fun setGameFinished() {
        dataManager.localPreferencesHelper.setGameStatus(false)
    }

    fun checkIsGameStarted() {
        val isGameStarted = dataManager.localPreferencesHelper.isGameStarted
        if (isGameStarted) {
            playersListView?.showStartContinueDialog()
        }
    }

    fun getPlayersList() {
        disposable = dataManager.getPlayers(SortType.NONE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { players -> playersListView?.setPlayersList(players) }
    }

    override fun detachView() {
        playersListView = null
        disposable?.dispose()
    }
}

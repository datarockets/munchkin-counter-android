package com.datarockets.mnchkn.ui.players

import android.content.Intent
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import butterknife.*
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.BaseActivity
import com.datarockets.mnchkn.ui.dashboard.DashboardActivity
import com.datarockets.mnchkn.ui.dialogs.NewPlayerDialogFragment
import com.datarockets.mnchkn.ui.dialogs.PlayerActionsDialogFragment
import com.datarockets.mnchkn.ui.editplayer.EditPlayerDialogFragment
import com.datarockets.mnchkn.ui.settings.SettingsActivity
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener
import timber.log.Timber
import javax.inject.Inject

class PlayersListActivity : BaseActivity(), PlayersListView,
        NewPlayerDialogFragment.AddNewPlayerDialogInterface,
        ColorPickerDialogListener {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.lv_player_list) lateinit var lvPlayersList: ListView
    @BindView(R.id.fab_add_player) lateinit var fabAddPlayer: FloatingActionButton

    @Inject lateinit var lvPlayerEditorListAdapter: PlayerEditorListAdapter
    @Inject lateinit var presenter: PlayersListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        setContentView(R.layout.activity_players)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        lvPlayersList.adapter = lvPlayerEditorListAdapter
        presenter.attachView(this)
        presenter.checkIsGameStarted()
        presenter.getPlayersList()
    }

    override fun onResume() {
        super.onResume()
        trackWithProperties("Current activity", "Activity name", "PlayersListActivity")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.players_list_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun addPlayerToList(player: Player) {
        lvPlayerEditorListAdapter.addPlayer(player)
    }

    override fun deletePlayerFromList(position: Int) {
        lvPlayerEditorListAdapter.deletePlayer(position)
    }

    override fun setPlayersList(players: List<Player>) {
        lvPlayerEditorListAdapter.setPlayers(players)
    }

    override fun showAddNewPlayerDialog() {
        val dialog = NewPlayerDialogFragment()
        dialog.show(supportFragmentManager, "NewPlayerDialogFragment")
    }

    override fun launchDashboard() {
        presenter.setGameStarted()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    @OnItemClick(R.id.lv_player_list)
    fun onItemClick(playerId: Long) {
        val editPlayerDialogFragment = EditPlayerDialogFragment.newInstance(playerId)
        editPlayerDialogFragment.show(supportFragmentManager, "EditPlayerDialogFragment")
    }

    @OnItemLongClick(R.id.lv_player_list)
    fun onItemLongClick(position: Int, playerId: Long): Boolean {
        val playerActionDialogFragment = PlayerActionsDialogFragment()
        playerActionDialogFragment.show(supportFragmentManager, "PlayerActionDialogFragment")
//
//        val alertDialog = AlertDialog.Builder(this)
//        alertDialog.apply {
//            setTitle(R.string.dialog_player_delete_title)
//            setMessage(R.string.dialog_player_delete_message)
//            setPositiveButton(R.string.button_yes) { dialog, which ->
//                presenter.deletePlayerListItem(position, itemId)
//            }
//            setNegativeButton(R.string.button_no) { dialog, which ->
//                dialog.dismiss()
//            }
//            create()
//            show()
//        }
        return true
    }

    @OnClick(R.id.fab_add_player)
    fun onAddNewPlayerClick() {
        showAddNewPlayerDialog()
    }

    override fun showStartContinueDialog() {
        val startContinueDialog = AlertDialog.Builder(this)
        startContinueDialog.apply {
            setTitle(R.string.dialog_start_continue_game_title)
            setMessage(R.string.dialog_start_continue_game_message)
            setPositiveButton(R.string.button_continue) { dialog, which -> launchDashboard() }
            setNegativeButton(R.string.button_start) { dialog, which ->
                dialog.dismiss()
                presenter.setGameFinished()
                presenter.clearPlayersStats()
                presenter.clearGameSteps()
            }
            setCancelable(false)
            create()
            show()
        }
    }

    override fun showWarning() {
        Toast.makeText(this, R.string.text_player_add_warning, Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_start_game -> presenter.checkIsEnoughPlayers()
            R.id.item_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFinishEditDialog(inputName: String) {
        presenter.addPlayer(inputName)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onColorSelected(dialogId: Int, @ColorInt color: Int) {
        Timber.d("Color selected: " + color)
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

}

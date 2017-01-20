package com.datarockets.mnchkn.ui.players

import android.content.Intent
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.BaseActivity
import com.datarockets.mnchkn.ui.dashboard.DashboardActivity
import com.datarockets.mnchkn.ui.dialogs.NewPlayerDialogFragment
import com.datarockets.mnchkn.ui.dialogs.PlayerActionsDialogFragment
import com.datarockets.mnchkn.ui.settings.SettingsActivity
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener
import com.woxthebox.draglistview.DragListView
import timber.log.Timber
import javax.inject.Inject

class PlayersListActivity : BaseActivity(), PlayersListView,
        NewPlayerDialogFragment.AddNewPlayerDialogInterface,
        ColorPickerDialogListener,
        PlayerEditorListAdapter.OnItemClickListener {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.lv_player_list) lateinit var lvPlayersList: DragListView
    @BindView(R.id.fab_add_player) lateinit var fabAddPlayer: FloatingActionButton

    @Inject lateinit var lvPlayerEditorListAdapter: PlayerEditorListAdapter
    @Inject lateinit var playersListPresenter: PlayersListPresenter
    @Inject lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        setContentView(R.layout.activity_players)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        lvPlayersList.setLayoutManager(linearLayoutManager)
        lvPlayersList.setAdapter(lvPlayerEditorListAdapter, false)
        lvPlayerEditorListAdapter.setOnItemClickListener(this)
        playersListPresenter.attachView(this)
        playersListPresenter.checkIsGameStarted()
        playersListPresenter.getPlayersList()
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
        playersListPresenter.setGameStarted()
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
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
                playersListPresenter.setGameFinished()
                playersListPresenter.clearPlayersStats()
                playersListPresenter.clearGameSteps()
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
            R.id.item_start_game -> playersListPresenter.checkIsEnoughPlayers()
            R.id.item_settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFinishEditDialog(inputName: String) {
        playersListPresenter.addPlayer(inputName)
    }

    override fun onDestroy() {
        super.onDestroy()
        playersListPresenter.detachView()
    }

    override fun onColorSelected(dialogId: Int, @ColorInt color: Int) {
        Timber.d("Color selected: " + color)
    }

    override fun onDialogDismissed(dialogId: Int) {

    }

    override fun onItemClick(itemView: View, position: Int) {
        val selectedPlayerId = lvPlayerEditorListAdapter.getItemId(position)
        val playerActionDialogFragment = PlayerActionsDialogFragment()
        playerActionDialogFragment.show(supportFragmentManager, "PlayerActionDialogFragment")
    }

}

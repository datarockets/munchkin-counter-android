package com.datarockets.mnchkn.ui.players

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
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
import com.datarockets.mnchkn.ui.editplayer.EditPlayerDialogFragment
import com.datarockets.mnchkn.ui.players.helpers.ItemTouchHelperCallback
import com.datarockets.mnchkn.ui.settings.SettingsActivity
import javax.inject.Inject

class PlayersListActivity : BaseActivity(), PlayersListView,
        NewPlayerDialogFragment.NewPlayerDialogListener,
        EditPlayerDialogFragment.EditPlayerDialogListener,
        PlayerEditorListAdapter.OnItemClickListener,
        PlayerEditorListAdapter.OnItemCheckboxClickListener,
        PlayerEditorListAdapter.OnStartDragListener,
        PlayerEditorListAdapter.OnItemMovedListener,
        PlayerActionsDialogFragment.PlayerActionsListener {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.lv_player_list) lateinit var lvPlayersList: RecyclerView
    @BindView(R.id.fab_add_player) lateinit var fabAddPlayer: FloatingActionButton

    @Inject lateinit var lvPlayerEditorListAdapter: PlayerEditorListAdapter
    @Inject lateinit var playersListPresenter: PlayersListPresenter
    @Inject lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemTouchHelperCallback: ItemTouchHelper.Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        setContentView(R.layout.activity_players)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)

        lvPlayersList.apply {
            adapter = lvPlayerEditorListAdapter
            layoutManager = linearLayoutManager
        }

        lvPlayerEditorListAdapter.apply {
            setOnItemClickListener(this@PlayersListActivity)
            setOnItemCheckboxClickListener(this@PlayersListActivity)
            setOnStartDragListener(this@PlayersListActivity)
            setOnItemMovedListener(this@PlayersListActivity)
        }

        mItemTouchHelperCallback= ItemTouchHelperCallback(lvPlayerEditorListAdapter)
        mItemTouchHelper = ItemTouchHelper(mItemTouchHelperCallback)
        mItemTouchHelper.attachToRecyclerView(lvPlayersList)

        playersListPresenter.apply {
            attachView(this@PlayersListActivity)
            checkIsGameStarted()
            getPlayersList()
        }
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

    override fun deletePlayerFromList(playerId: Long) {
        lvPlayerEditorListAdapter.deletePlayer(playerId)
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
        AlertDialog.Builder(this).apply {
            setTitle(R.string.dialog_start_continue_game_title)
            setMessage(R.string.dialog_start_continue_game_message)
            setPositiveButton(R.string.button_continue) { dialog, which -> launchDashboard() }
            setNegativeButton(R.string.button_start) { dialog, which ->
                dialog.dismiss()
                playersListPresenter.apply {
                    setGameFinished()
                    clearPlayersStats()
                    clearGameSteps()
                }
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
        val playersCount = lvPlayerEditorListAdapter.itemCount
        playersListPresenter.addPlayer(inputName, playersCount)
    }

    override fun onPlayerItemClick(playerId: Long) {
        val playerActionDialogFragment = PlayerActionsDialogFragment.newInstance(playerId)
        playerActionDialogFragment.show(supportFragmentManager, "PlayerActionDialogFragment")
    }

    override fun onPlayerItemCheckboxClick(playerId: Long, isPlaying: Boolean) {
        playersListPresenter.markPlayerAsPlaying(playerId, isPlaying)
    }

    override fun onEditPlayer(playerId: Long) {
        val editPlayerDialogFragment = EditPlayerDialogFragment.newInstance(playerId)
        editPlayerDialogFragment.show(supportFragmentManager, "EditPlayerDialogFragment")
    }

    override fun onDeletePlayer(playerId: Long) {
        playersListPresenter.deletePlayerListItem(playerId)
    }

    override fun onEditedPlayerName(playerId: Long, playerName: String) {
        lvPlayerEditorListAdapter.updatePlayerName(playerId, playerName)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mItemTouchHelper.startDrag(viewHolder)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        val movedPlayerId = lvPlayerEditorListAdapter.getItemId(fromPosition)
        val draggerPlayerId = lvPlayerEditorListAdapter.getItemId(toPosition)
        playersListPresenter.changePlayerPosition(draggerPlayerId, toPosition)
        playersListPresenter.changePlayerPosition(movedPlayerId, fromPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        playersListPresenter.detachView()
    }

}

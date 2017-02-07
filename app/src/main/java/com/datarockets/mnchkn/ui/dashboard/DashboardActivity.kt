package com.datarockets.mnchkn.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemClick
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.BaseActivity
import com.datarockets.mnchkn.ui.dialogs.RollDiceDialogFragment
import com.datarockets.mnchkn.ui.player.PlayerFragment
import com.datarockets.mnchkn.ui.player.PlayerListAdapter
import com.datarockets.mnchkn.ui.result.GameResultActivity
import javax.inject.Inject

class DashboardActivity : BaseActivity(), DashboardView, PlayerFragment.OnScoresChangedListener {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.lv_player_list) lateinit var lvPlayerList: ListView
    @BindView(R.id.btn_next_player) lateinit var btnNextStep: Button

    @Inject lateinit var lvPlayerListAdapter: PlayerListAdapter
    @Inject lateinit var presenter: DashboardPresenter
    private lateinit var playerFragment: PlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        setContentView(R.layout.activity_dashboard)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        lvPlayerList.adapter = lvPlayerListAdapter
        playerFragment = supportFragmentManager.findFragmentById(R.id.fragment_player) as PlayerFragment
        presenter.apply {
            attachView(this@DashboardActivity)
            checkIsScreenShouldBeOn()
            getPlayingPlayers()
        }
    }

    override fun onResume() {
        super.onResume()
        trackWithProperties("Current activity", "Activity name", "DashboardActivity")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_finish_game -> showConfirmFinishGameDialog()
            R.id.item_roll_dice -> showRollDiceDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finishGame() {
        presenter.setGameFinished()
        val intent = Intent(this, GameResultActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun setPlayers(players: List<Player>) {
        lvPlayerListAdapter.setPlayers(players)
        lvPlayerList.apply {
            setSelection(0)
            setItemChecked(0, true)
        }
        playerFragment.loadPlayerScores(lvPlayerListAdapter.getItemId(0), 0)
    }

    override fun showConfirmFinishGameDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.dialog_finish_game_title)
            setMessage(R.string.dialog_finish_game_message)
            setPositiveButton(R.string.button_yes) { dialog, which -> finishGame() }
            setNegativeButton(R.string.button_no) { dialog, which -> dialog.dismiss() }
            create()
            show()
        }
    }

    override fun showRollDiceDialog() {
        val rollDiceDialogFragment = RollDiceDialogFragment()
        rollDiceDialogFragment.show(supportFragmentManager, "RollDiceDialogFragment")
    }

    override fun updatePlayerInformation(player: Player, position: Int) {
        val playerToUpdate = lvPlayerListAdapter.getItem(position)
        playerToUpdate.levelScore = player.levelScore
        playerToUpdate.strengthScore = player.strengthScore
        lvPlayerListAdapter.notifyDataSetChanged()
    }

    override fun keepScreenOn(keepActive: Boolean) {
        val keepScreenOnFlag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        window.apply {
            if (keepActive) {
                addFlags(keepScreenOnFlag)
            } else {
                clearFlags(keepScreenOnFlag)
            }
        }
    }

    @OnClick(R.id.btn_next_player)
    fun nextPlayer() {
        var position = lvPlayerList.checkedItemPosition
        if (position == lvPlayerList.count - 1) {
            lvPlayerList.setItemChecked(0, true)
            lvPlayerList.setSelection(0)
            position = 0
        } else {
            position++
            lvPlayerList.setItemChecked(position, true)
            lvPlayerList.setSelection(position)
        }
        playerFragment.loadPlayerScores(lvPlayerListAdapter.getItemId(position), position)
    }

    @OnItemClick(R.id.lv_player_list)
    fun onItemClick(position: Int) {
        lvPlayerList.setItemChecked(position, true)
        lvPlayerList.setSelection(position)
        playerFragment.loadPlayerScores(lvPlayerListAdapter.getItemId(position), position)
    }

    override fun onScoresChanged(playerPosition: Int, playerLevel: Int, playerStrength: Int) {
        val playerId = lvPlayerListAdapter.getItemId(playerPosition)
        lvPlayerListAdapter.updatePlayerScores(playerPosition, playerLevel, playerStrength)
        presenter.insertStep(playerId, playerLevel, playerStrength)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
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

class DashboardActivity : BaseActivity(), DashboardView, PlayerFragment.PlayerFragmentCallback {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.lv_player_list) lateinit var lvPlayerList: ListView
    @BindView(R.id.btn_next_step) lateinit var btnNextStep: Button

    @Inject lateinit var lvPlayerListAdapter: PlayerListAdapter
    @Inject lateinit var presenter: DashboardPresenter

    lateinit var playerFragment: PlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        setContentView(R.layout.activity_dashboard)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        lvPlayerList.adapter = lvPlayerListAdapter
        playerFragment = supportFragmentManager.findFragmentById(R.id.fragment_player) as PlayerFragment
        presenter.attachView(this)
        presenter.checkIsScreenShouldBeOn()
    }

    override fun onResume() {
        super.onResume()
        trackWithProperties("Current activity", "Activity name", "DashboardActivity")
        presenter.getPlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_finish_game -> showConfirmFinishGameDialog()
            R.id.item_roll_dice -> {
                showRollDiceDialog()
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
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
        lvPlayerListAdapter.addPlayers(players)
        lvPlayerList.setSelection(0)
        lvPlayerList.setItemChecked(0, true)
    }

    override fun showConfirmFinishGameDialog() {
        val confirmFinishGameDialog = AlertDialog.Builder(this)
                .setTitle(R.string.dialog_finish_game_title)
                .setMessage(R.string.dialog_finish_game_message)
                .setPositiveButton(R.string.button_yes) { dialog, which -> finishGame() }
                .setNegativeButton(R.string.button_no) { dialog, which -> dialog.dismiss() }
        confirmFinishGameDialog.create().show()
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
        if (keepActive) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    @OnClick(R.id.btn_next_step)
    fun nextStep() {
        var position = lvPlayerList.checkedItemPosition
        if (position == lvPlayerList.count - 1) {
            lvPlayerList.setItemChecked(0, true)
            lvPlayerList.setSelection(0)
//            position = 0
        } else {
            position++
            lvPlayerList.setItemChecked(position, true)
            lvPlayerList.setSelection(position)
        }
        //        playerFragment.loadPlayerScores(lvPlayerListAdapter.getItem(position), position);
    }

    @OnItemClick(R.id.lv_player_list)
    fun onItemClick(position: Int) {
        lvPlayerList.setItemChecked(position, true)
        //        playerFragment.loadPlayerScores(lvPlayerListAdapter.getItem(position), position);
    }

    override fun onScoreChanged(player: Player, index: Int) {
        presenter.updatePlayerInformation(player, index)
        presenter.insertStep(player)
    }

}

package com.datarockets.mnchkn.injection.components

import com.datarockets.mnchkn.injection.PerActivity
import com.datarockets.mnchkn.injection.modules.ActivityModule
import com.datarockets.mnchkn.ui.SplashActivity
import com.datarockets.mnchkn.ui.charts.ChartsFragment
import com.datarockets.mnchkn.ui.dashboard.DashboardActivity
import com.datarockets.mnchkn.ui.dialogs.NewPlayerDialogFragment
import com.datarockets.mnchkn.ui.dialogs.PlayerActionsDialogFragment
import com.datarockets.mnchkn.ui.dialogs.RollDiceDialogFragment
import com.datarockets.mnchkn.ui.editplayer.EditPlayerDialogFragment
import com.datarockets.mnchkn.ui.player.PlayerFragment
import com.datarockets.mnchkn.ui.players.PlayersListActivity
import com.datarockets.mnchkn.ui.result.GameResultActivity

import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(splashActivity: SplashActivity)
    fun inject(dashboardActivity: DashboardActivity)
    fun inject(gameResultActivity: GameResultActivity)
    fun inject(playersListActivity: PlayersListActivity)

    fun inject(playerFragment: PlayerFragment)
    fun inject(chartsFragment: ChartsFragment)
    fun inject(rollDiceDialogFragment: RollDiceDialogFragment)
    fun inject(editPlayerDialogFragment: EditPlayerDialogFragment)
    fun inject(newPlayerDialogFragment: NewPlayerDialogFragment)
    fun inject(playerActionsDialogFragment: PlayerActionsDialogFragment)
}

package com.datarockets.mnchknlvlcntr.injection.components

import com.datarockets.mnchknlvlcntr.injection.PerActivity
import com.datarockets.mnchknlvlcntr.injection.modules.ActivityModule
import com.datarockets.mnchknlvlcntr.ui.SplashActivity
import com.datarockets.mnchknlvlcntr.ui.charts.ChartsFragment
import com.datarockets.mnchknlvlcntr.ui.dashboard.DashboardActivity
import com.datarockets.mnchknlvlcntr.ui.dialogs.NewPlayerDialogFragment
import com.datarockets.mnchknlvlcntr.ui.dialogs.PlayerActionsDialogFragment
import com.datarockets.mnchknlvlcntr.ui.dialogs.RollDiceDialogFragment
import com.datarockets.mnchknlvlcntr.ui.editplayer.EditPlayerDialogFragment
import com.datarockets.mnchknlvlcntr.ui.onboard.WelcomeActivity
import com.datarockets.mnchknlvlcntr.ui.player.PlayerFragment
import com.datarockets.mnchknlvlcntr.ui.players.PlayersListActivity
import com.datarockets.mnchknlvlcntr.ui.result.GameResultActivity

import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    fun inject(splashActivity: SplashActivity)
    fun inject(welcomeActivity: WelcomeActivity)
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

package com.datarockets.mnchkn.ui.onboard

import android.content.Intent
import android.os.Bundle
import com.chyrta.onboarder.OnboarderActivity
import com.chyrta.onboarder.OnboarderPage
import com.datarockets.mnchkn.MunchkinApplication
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.injection.components.ActivityComponent
import com.datarockets.mnchkn.injection.components.DaggerActivityComponent
import com.datarockets.mnchkn.injection.modules.ActivityModule
import com.datarockets.mnchkn.ui.players.PlayersListActivity
import java.util.*
import javax.inject.Inject

class WelcomeActivity : OnboarderActivity(), WelcomeView {

    private lateinit var onboarderPages: MutableList<OnboarderPage>
    private lateinit var onboarderPageOne: OnboarderPage
    private lateinit var onboarderPageTwo: OnboarderPage
    private lateinit var onboarderPageThree: OnboarderPage
    private lateinit var activityComponent: ActivityComponent

    @Inject lateinit var welcomePresenter: WelcomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(MunchkinApplication[this].mApplicationComponent)
                .activityModule(ActivityModule(this))
                .build()
        activityComponent.inject(this)

        welcomePresenter.attachView(this)
        welcomePresenter.checkIsOnboardingSeen()
        onboarderPages = ArrayList<OnboarderPage>()
        onboarderPageOne = OnboarderPage(
                R.string.onboarder_page1_title,
                R.string.onboarder_page1_description,
                R.drawable.ic_munchkin)
        onboarderPageOne.setBackgroundColor(R.color.card_general)
        onboarderPageTwo = OnboarderPage(
                R.string.onboarder_page2_title,
                R.string.onboarder_page2_description,
                R.drawable.ic_infinite)
        onboarderPageTwo.setBackgroundColor(R.color.card_light)
        onboarderPageThree = OnboarderPage(
                R.string.onboarder_page3_title,
                R.string.onboarder_page3_description,
                R.drawable.ic_dice)
        onboarderPageThree.setBackgroundColor(R.color.card_corner)
        onboarderPages.add(onboarderPageOne)
        onboarderPages.add(onboarderPageTwo)
        onboarderPages.add(onboarderPageThree)
        setOnboardPagesReady(onboarderPages)
    }

    public override fun onSkipButtonPressed() {
        super.onSkipButtonPressed()
    }

    override fun onFinishButtonPressed() {
        openPlayersActivity()
    }

    override fun openPlayersActivity() {
        welcomePresenter.setOnboardingSeen()
        val intent = Intent(this, PlayersListActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        welcomePresenter.detachView()
    }
}

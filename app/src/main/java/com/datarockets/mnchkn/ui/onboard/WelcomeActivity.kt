package com.datarockets.mnchkn.ui.onboard


import android.content.Intent
import android.os.Bundle
import com.chyrta.onboarder.OnboarderActivity
import com.chyrta.onboarder.OnboarderPage
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.ui.players.PlayersListActivity
import java.util.*

class WelcomeActivity : OnboarderActivity(), WelcomeView {

    lateinit var onboarderPages: MutableList<OnboarderPage>
    lateinit var onboarderPageOne: OnboarderPage
    lateinit var onboarderPageTwo: OnboarderPage
    lateinit var onboarderPageThree: OnboarderPage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val intent = Intent(this, PlayersListActivity::class.java)
        startActivity(intent)
        finish()
    }

}

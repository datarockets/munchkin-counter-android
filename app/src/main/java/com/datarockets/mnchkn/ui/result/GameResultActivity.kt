package com.datarockets.mnchkn.ui.result

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.ui.base.BaseActivity
import com.datarockets.mnchkn.ui.players.PlayersListActivity
import javax.inject.Inject

class GameResultActivity : BaseActivity(), GameResultView {

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.vp_charts) lateinit var vpCharts: ViewPager
    @BindView(R.id.tl_charts_title) lateinit var tlChartsTitle: TabLayout

    @Inject lateinit var presenter: GameResultPresenter
    @Inject lateinit var vpChartsAdapter: ChartsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent().inject(this)
        setContentView(R.layout.activity_game_result)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        presenter.attachView(this)
        loadChartFragments()
    }

    override fun onResume() {
        super.onResume()
        trackWithProperties("Current activity", "Activity name", "GameResultActivity")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.clearGameResults()
        val intent = Intent(this, PlayersListActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun loadChartFragments() {
        vpChartsAdapter = ChartsPagerAdapter(supportFragmentManager, this)
        vpCharts.apply {
            adapter = vpChartsAdapter
            offscreenPageLimit = 3
        }
        tlChartsTitle.setupWithViewPager(vpCharts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_result_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.item_share_app -> presenter.generateShareResultLink()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun launchShareableIntent(shareableIntent: Intent) {
        startActivity(Intent.createChooser(shareableIntent, "Share an app"))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            presenter.clearGameResults()
        }
        presenter.detachView()
    }
}

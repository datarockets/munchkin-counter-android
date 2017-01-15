package com.datarockets.mnchkn.ui.result

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
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
        presenter.onBackPressed()
        val intent = Intent(this, PlayersListActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun loadChartFragments() {
        vpChartsAdapter = ChartsPagerAdapter(supportFragmentManager, this)
        vpCharts.adapter = vpChartsAdapter
        vpCharts.offscreenPageLimit = 3
        tlChartsTitle.setupWithViewPager(vpCharts)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}

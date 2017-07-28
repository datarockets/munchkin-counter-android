package com.datarockets.mnchknlvlcntr.ui.charts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.datarockets.mnchknlvlcntr.R
import com.datarockets.mnchknlvlcntr.data.models.Player
import com.datarockets.mnchknlvlcntr.ui.base.BaseActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import javax.inject.Inject

class ChartsFragment : Fragment(), ChartsView {

    @BindView(R.id.line_chart_view) lateinit var lineChartView: LineChart
    @BindView(R.id.lv_player_list) lateinit var lvPlayerList: ListView

    @Inject lateinit var lvPlayerListAdapter: PlayerChartListAdapter
    @Inject lateinit var presenter: ChartsPresenter

    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, bundle: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_charts, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
        lvPlayerList.adapter = lvPlayerListAdapter
        presenter.apply {
            attachView(this@ChartsFragment)
            loadChartData(arguments.getInt(CHART_TYPE))
            loadPlayers(arguments.getInt(CHART_TYPE))
        }
    }

    override fun showPlayersChart(lineData: LineData) {
        lineChartView.xAxis.granularity = 1f
        lineChartView.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChartView.axisLeft.granularity = 1f
        lineChartView.setDrawGridBackground(false)
        lineChartView.description.isEnabled = false
        lineChartView.data = lineData
        lineChartView.invalidate()
    }

    override fun showPlayersList(players: List<Player>) {
        lvPlayerListAdapter.setPlayers(players, arguments.getInt(CHART_TYPE))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
        presenter.detachView()
    }

    companion object {

        private val CHART_TYPE = "chart_type"

        fun newInstance(type: Int): ChartsFragment {
            val args = Bundle()
            args.putInt(CHART_TYPE, type)
            val fragment = ChartsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

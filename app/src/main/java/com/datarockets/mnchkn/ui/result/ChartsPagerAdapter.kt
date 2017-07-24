package com.datarockets.mnchkn.ui.result

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.injection.ActivityContext
import com.datarockets.mnchkn.ui.charts.ChartsFragment

import javax.inject.Inject

class ChartsPagerAdapter
@Inject constructor(fm: FragmentManager,
                    @ActivityContext private val context: Context) : FragmentStatePagerAdapter(fm) {

    private val titles = intArrayOf(R.string.tab_level, R.string.tab_strength, R.string.tab_summary)

    override fun getItem(position: Int): Fragment {
        return ChartsFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(titles[position])
    }
}

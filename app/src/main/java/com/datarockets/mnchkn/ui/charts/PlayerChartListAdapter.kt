package com.datarockets.mnchkn.ui.charts

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.amulyakhare.textdrawable.TextDrawable
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Player
import java.util.*
import javax.inject.Inject

class PlayerChartListAdapter
@Inject constructor() : BaseAdapter() {

    private var playersList: List<Player> = ArrayList()
    private var type: Int = 0

    fun setPlayers(players: List<Player>, orderType: Int) {
        playersList = players
        type = orderType
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return playersList.size
    }

    override fun getItem(i: Int): Player {
        return playersList[i]
    }

    override fun getItemId(i: Int): Long {
        return playersList[i].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        val player = getItem(position)

        if (convertView != null) {
            holder = convertView.tag as ViewHolder
        } else {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_player_chart, null)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        }

        val color = Color.parseColor(player.color)
        val capitalizedPlayerFirstLetter = player.name!!.substring(0, 1).toUpperCase()
        val drawable = TextDrawable.builder().buildRound(capitalizedPlayerFirstLetter, color)

        holder.ivPlayerColor.setImageDrawable(drawable)
        holder.tvPlayerName.text = player.name
        when (type) {
            ORDER_BY_LEVEL -> {
                val levelScore = player.levelScore.toString()
                holder.tvPlayerScore.text = levelScore
            }
            ORDER_BY_STRENGTH -> {
                val strengthScore = player.strengthScore.toString()
                holder.tvPlayerScore.text = strengthScore
            }
            ORDER_BY_TOTAL -> {
                val totalScoreAmount = player.levelScore + player.strengthScore
                val totalScore = totalScoreAmount.toString()
                holder.tvPlayerScore.text = totalScore
            }
        }
        return convertView
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.iv_player_color) lateinit var ivPlayerColor: ImageView
        @BindView(R.id.tv_player_name) lateinit var tvPlayerName: TextView
        @BindView(R.id.tv_player_score) lateinit var tvPlayerScore: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

    companion object {
        private val ORDER_BY_LEVEL = 0
        private val ORDER_BY_STRENGTH = 1
        private val ORDER_BY_TOTAL = 2
    }
}

package com.datarockets.mnchkn.ui.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Player
import java.util.*
import javax.inject.Inject


class PlayerListAdapter
@Inject constructor() : BaseAdapter() {

    private val mPlayers: MutableList<Player> = ArrayList()

    fun addPlayers(players: List<Player>) {
        mPlayers.addAll(players)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mPlayers.size
    }

    override fun getItem(position: Int): Player {
        return mPlayers[position]
    }

    override fun getItemId(i: Int): Long {
        return mPlayers[i].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        val player = mPlayers[position]

        if (convertView != null) {
            holder = convertView.tag as ViewHolder
        } else {
            convertView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.player_list_item, parent, false)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        }

        holder.tvPlayerName.text = player.name
        holder.tvPlayerLevelScore.text = player.levelScore.toString()
        holder.tvPlayerStrengthScore.text = player.strengthScore.toString()
        return convertView!!
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.tv_player_item_name) lateinit var tvPlayerName: TextView
        @BindView(R.id.tv_player_item_level_score) lateinit var tvPlayerLevelScore: TextView
        @BindView(R.id.tv_player_item_strength_score) lateinit var tvPlayerStrengthScore: TextView

        init {
            ButterKnife.bind(this, view)
        }

    }
}

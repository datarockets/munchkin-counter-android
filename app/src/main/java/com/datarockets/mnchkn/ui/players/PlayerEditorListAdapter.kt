package com.datarockets.mnchkn.ui.players

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

class PlayerEditorListAdapter
@Inject constructor() : BaseAdapter() {

    private val mPlayersList: MutableList<Player> = ArrayList()

    fun addPlayer(player: Player) {
        mPlayersList.add(player)
        notifyDataSetChanged()
    }

    fun setPlayers(players: List<Player>) {
        mPlayersList.addAll(players)
        notifyDataSetChanged()
    }

    fun deletePlayer(position: Int) {
        mPlayersList.removeAt(position)
        notifyDataSetChanged()
    }

    fun updatePlayer(position: Int, player: Player) {
        mPlayersList[position] = player
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mPlayersList.size
    }

    override fun getItem(position: Int): Player {
        return mPlayersList[position]
    }

    override fun getItemId(i: Int): Long {
        return mPlayersList[i].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder

        val player = mPlayersList[position]
        if (convertView != null) {
            holder = convertView.tag as ViewHolder
        } else {
            val convertView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.player_item, parent, false)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        }

        val color = Color.parseColor(player.color)
        val capitalizedPlayerFirstLetter = player.name!!.substring(0, 1).toUpperCase()
        val drawable = TextDrawable.builder()
                .buildRound(capitalizedPlayerFirstLetter, color)
        holder.ivPlayerImage.setImageDrawable(drawable)
        holder.tvPlayerName.text = player.name
        return convertView!!
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.iv_player_color) lateinit var ivPlayerImage: ImageView
        @BindView(R.id.tv_player_name) lateinit var tvPlayerName: TextView

        init {
            ButterKnife.bind(this, view)
        }
    }

}

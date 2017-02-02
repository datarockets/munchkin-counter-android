package com.datarockets.mnchkn.ui.players

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.amulyakhare.textdrawable.TextDrawable
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Player
import com.woxthebox.draglistview.DragItemAdapter
import javax.inject.Inject

class PlayerEditorListAdapter
@Inject constructor() : DragItemAdapter<Player, PlayerEditorListAdapter.ViewHolder>() {

    init {
        setHasStableIds(true)
        itemList = mutableListOf()
    }

    interface OnItemClickListener {
        fun onPlayerItemClick(playerId: Long)
    }

    interface OnItemCheckboxClickListener {
        fun onPlayerItemCheckboxClick(playerId: Long, isPlaying: Boolean)
    }

    var mClickListener: OnItemClickListener? = null
    var mCheckboxClickListener: OnItemCheckboxClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    fun setOnItemCheckboxClickListener(listener: OnItemCheckboxClickListener) {
        mCheckboxClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.player_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        super.onBindViewHolder(holder, position)

        val player = itemList[position]

        val color = Color.parseColor(player.color)
        val capitalizedPlayerFirstLetter = player.name!!.substring(0, 1).toUpperCase()
        val drawable = TextDrawable.builder()
                .buildRound(capitalizedPlayerFirstLetter, color)
        holder?.ivPlayerImage?.setImageDrawable(drawable)
        holder?.tvPlayerName?.text = player.name
        holder?.cbIsPlaying?.isChecked = player.playing
    }

    fun addPlayer(player: Player) {
        addItem(itemList.count(), player)
    }

    fun setPlayers(players: List<Player>) {
        itemList = players
    }

    fun deletePlayer(playerId: Long) {
        val position = getPositionForItemId(playerId)
        removeItem(position)
    }

    fun updatePlayerName(playerId: Long, playerName: String) {
        val position = getPositionForItemId(playerId)
        itemList[position].name = playerName
        notifyItemChanged(position)
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].id
    }

    inner class ViewHolder(view: View) : DragItemAdapter.ViewHolder(view, R.id.ll_player_list_item, true) {
        @BindView(R.id.iv_player_color) lateinit var ivPlayerImage: ImageView
        @BindView(R.id.tv_player_name) lateinit var tvPlayerName: TextView
        @BindView(R.id.cb_is_playing) lateinit var cbIsPlaying: CheckBox

        init {
            ButterKnife.bind(this, view)
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val playerId = getItemId(position)
                    mClickListener?.onPlayerItemClick(playerId)
                }
            }
            cbIsPlaying.setOnCheckedChangeListener { compoundButton, b ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val playerId = getItemId(position)
                    mCheckboxClickListener?.onPlayerItemCheckboxClick(playerId, b)
                }
            }
        }

    }

}

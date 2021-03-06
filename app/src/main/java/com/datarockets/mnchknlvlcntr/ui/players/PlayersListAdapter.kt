package com.datarockets.mnchknlvlcntr.ui.players

import android.graphics.Color
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.amulyakhare.textdrawable.TextDrawable
import com.datarockets.mnchknlvlcntr.R
import com.datarockets.mnchknlvlcntr.data.models.Player
import com.datarockets.mnchknlvlcntr.ui.players.helpers.ItemTouchHelperAdapter
import com.datarockets.mnchknlvlcntr.ui.players.helpers.ItemTouchHelperViewHolder
import java.util.*
import javax.inject.Inject

class PlayersListAdapter
@Inject constructor() : RecyclerView.Adapter<PlayersListAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    private val players = mutableListOf<Player>()

    init {
        setHasStableIds(true)
    }

    interface PlayersListListener {
        fun onPlayerItemClick(playerId: Long)
        fun onPlayerItemCheckboxClick(playerId: Long, isPlaying: Boolean)
        fun onItemMoved(fromPosition: Int, toPosition: Int)
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    var listener: PlayersListListener? = null

    fun setPlayersListListener(listener: PlayersListListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_player, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val player = players[position]

        val color = Color.parseColor(player.color)
        val capitalizedPlayerFirstLetter = player.name!!.substring(0, 1).toUpperCase()
        val drawable = TextDrawable.builder().buildRound(capitalizedPlayerFirstLetter, color)

        holder?.ivPlayerImage?.setImageDrawable(drawable)
        holder?.tvPlayerName?.text = player.name
        holder?.cbIsPlaying?.isChecked = player.playing

        holder?.ivReorder?.setOnTouchListener { view, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                listener?.onStartDrag(holder)
            }
            false
        }
    }

    fun addPlayer(player: Player) {
        players.add(players.count(), player)
        notifyDataSetChanged()
    }

    fun setPlayers(players: List<Player>) {
        this.players.addAll(players)
        notifyDataSetChanged()
    }

    fun deletePlayer(playerId: Long) {
        val position = getPositionForItemId(playerId)
        players.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updatePlayerName(playerId: Long, playerName: String) {
        val position = getPositionForItemId(playerId)
        players[position].name = playerName
        notifyItemChanged(position)
    }

    override fun getItemId(position: Int): Long {
        return players[position].id
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        listener?.onItemMoved(fromPosition, toPosition)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(players, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        players.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getPositionForItemId(id: Long): Int {
        return (0..itemCount - 1).firstOrNull { id == players[it].id } ?: RecyclerView.NO_POSITION
    }

    fun hasPlayerWithId(playerId: Long): Boolean {
        return players.find { (id) -> id == playerId } != null
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
        @BindView(R.id.iv_reorder) lateinit var ivReorder: ImageView
        @BindView(R.id.iv_icon) lateinit var ivPlayerImage: ImageView
        @BindView(R.id.tv_name) lateinit var tvPlayerName: TextView
        @BindView(R.id.cb_playing) lateinit var cbIsPlaying: CheckBox

        init {
            ButterKnife.bind(this, view)
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val playerId = getItemId(position)
                    listener?.onPlayerItemClick(playerId)
                }
            }
            cbIsPlaying.setOnCheckedChangeListener { compoundButton, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val playerId = getItemId(position)
                    players[position].playing = isChecked
                    listener?.onPlayerItemCheckboxClick(playerId, isChecked)
                }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }
}

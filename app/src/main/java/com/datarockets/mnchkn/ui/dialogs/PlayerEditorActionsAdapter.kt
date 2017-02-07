package com.datarockets.mnchkn.ui.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.datarockets.mnchkn.R
import javax.inject.Inject

class PlayerEditorActionsAdapter
@Inject constructor() : BaseAdapter() {

    val actionItemsTitles: IntArray = intArrayOf(
            R.string.dialog_player_actions_edit_player,
            R.string.dialog_player_actions_delete_player
    )

    val actionItemsIcons: IntArray = intArrayOf(
            R.drawable.ic_edit,
            R.drawable.ic_delete
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView != null) {
            holder = convertView.tag as ViewHolder
        } else {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.player_action_item, parent, false)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        }

        holder.tvActionName.text = parent.context.getText(actionItemsTitles[position])
        holder.ivActionIcon.setImageResource(actionItemsIcons[position])

        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return actionItemsTitles.count()
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.iv_action_icon) lateinit var ivActionIcon: ImageView
        @BindView(R.id.tv_action_name) lateinit var tvActionName: TextView

        init {
            ButterKnife.bind(this, view)
        }

    }

}
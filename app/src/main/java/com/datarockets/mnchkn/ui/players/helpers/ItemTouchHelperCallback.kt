package com.datarockets.mnchkn.ui.players.helpers

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class ItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback() {

    private val ALPHA_FULL = 1.0f

    override fun onMoved(recyclerView: RecyclerView?,
                         viewHolder: RecyclerView.ViewHolder?,
                         fromPos: Int,
                         target: RecyclerView.ViewHolder?,
                         toPos: Int,
                         x: Int,
                         y: Int) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        mAdapter.onItemMoved(fromPos, toPos)
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView?,
                        source: RecyclerView.ViewHolder?,
                        target: RecyclerView.ViewHolder?): Boolean {
        if (source?.itemViewType != target?.itemViewType) {
            return false
        }

        mAdapter.onItemMove(source!!.adapterPosition, target!!.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        mAdapter.onItemDismiss(viewHolder!!.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is ItemTouchHelperViewHolder) {
                val itemViewHolder: ItemTouchHelperViewHolder = viewHolder
                itemViewHolder.onItemSelected()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        super.clearView(recyclerView, viewHolder)

        viewHolder?.itemView?.alpha = ALPHA_FULL

        if (viewHolder is ItemTouchHelperViewHolder) {
            val itemViewHolder: ItemTouchHelperViewHolder = viewHolder
            itemViewHolder.onItemClear()
        }

    }

}
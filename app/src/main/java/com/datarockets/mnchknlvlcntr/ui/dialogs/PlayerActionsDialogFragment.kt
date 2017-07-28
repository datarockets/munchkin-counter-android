package com.datarockets.mnchknlvlcntr.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnItemClick
import butterknife.Unbinder
import com.datarockets.mnchknlvlcntr.R
import com.datarockets.mnchknlvlcntr.ui.base.BaseActivity
import javax.inject.Inject

class PlayerActionsDialogFragment : BottomSheetDialogFragment() {

    @BindView(R.id.lv_player_actions) lateinit var lvPlayerActions: ListView

    @Inject lateinit var playerEditorActionsAdapter: PlayerEditorActionsAdapter
    private lateinit var unbinder: Unbinder

    private var playerId: Long = 0
    private var playerActionListener: PlayerActionsListener? = null

    interface PlayerActionsListener {
        fun onEditPlayer(playerId: Long)
        fun onDeletePlayer(playerId: Long)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
        playerId = arguments.getLong(PLAYER_ID)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        playerActionListener = (activity as PlayerActionsListener)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_player_action, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
        lvPlayerActions.adapter = playerEditorActionsAdapter
    }

    @OnItemClick(R.id.lv_player_actions)
    fun onActionItemSelected(position: Int) {
        when(position) {
            ACTION_EDIT -> {
                playerActionListener?.onEditPlayer(playerId)
                dismiss()
            }
            ACTION_DELETE -> {
                playerActionListener?.onDeletePlayer(playerId)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    companion object {

        private val PLAYER_ID = "player_id"
        private val ACTION_EDIT = 0
        private val ACTION_DELETE = 1

        fun newInstance(playerId: Long): PlayerActionsDialogFragment {
            val args = Bundle()
            args.putLong(PLAYER_ID, playerId)
            val fragment = PlayerActionsDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
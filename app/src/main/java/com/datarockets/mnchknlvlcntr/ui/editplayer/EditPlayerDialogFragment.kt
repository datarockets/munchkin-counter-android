package com.datarockets.mnchknlvlcntr.ui.editplayer

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import butterknife.*
import com.amulyakhare.textdrawable.TextDrawable
import com.datarockets.mnchknlvlcntr.R
import com.datarockets.mnchknlvlcntr.ui.base.BaseActivity
import javax.inject.Inject

class EditPlayerDialogFragment : BottomSheetDialogFragment(), EditPlayerView {

    private lateinit var unbinder: Unbinder
    private var playerId: Long = 0
    private var editPlayerDialogListener: EditPlayerDialogListener? = null

    @BindView(R.id.et_player_name) lateinit var etPlayerName: EditText
    @BindView(R.id.btn_update_player) lateinit var btnUpdatePlayer: ImageButton
    @BindView(R.id.iv_player_color) lateinit var ivPlayerColor: ImageView

    @Inject lateinit var editPlayerPresenter: EditPlayerPresenter

    interface EditPlayerDialogListener {
        fun onEditedPlayerName(playerId: Long, playerName: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
        playerId = arguments.getLong(PLAYER_ID)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        editPlayerDialogListener = (activity as EditPlayerDialogListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_edit_player, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
        editPlayerPresenter.attachView(this)
        editPlayerPresenter.loadPlayer(playerId)
    }

    @OnClick(R.id.btn_update_player)
    fun onUpdatePlayerClick() {
        editPlayerPresenter.updatePlayerName(playerId, etPlayerName.text.toString())
    }

    @OnEditorAction(R.id.et_player_name)
    fun onEditorAction(actionId: Int): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            editPlayerPresenter.updatePlayerName(playerId, etPlayerName.text.toString())
            return true
        }
        return false
    }

    override fun showPlayerInformation(playerName: String, playerColor: String) {
        etPlayerName.setText(playerName)
        etPlayerName.setSelection(playerName.length)
        val color = Color.parseColor(playerColor)
        val capitalizedPlayerFirstLetter = playerName.substring(0, 1).toUpperCase()
        val drawable = TextDrawable.builder()
                .buildRound(capitalizedPlayerFirstLetter, color)
        ivPlayerColor.setImageDrawable(drawable)
    }

    override fun hideEditPlayerDialog(playerId: Long, playerName: String) {
        editPlayerDialogListener?.onEditedPlayerName(playerId, playerName)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
        editPlayerPresenter.detachView()
    }

    companion object {

        val PLAYER_ID = "player_id"

        fun newInstance(playerId: Long): EditPlayerDialogFragment {
            val args = Bundle()
            args.putLong(PLAYER_ID, playerId)
            val fragment = EditPlayerDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

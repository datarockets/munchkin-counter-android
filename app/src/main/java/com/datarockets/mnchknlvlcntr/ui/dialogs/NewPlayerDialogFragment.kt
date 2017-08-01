package com.datarockets.mnchknlvlcntr.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import butterknife.*
import com.datarockets.mnchknlvlcntr.R
import com.datarockets.mnchknlvlcntr.ui.base.BaseActivity

class NewPlayerDialogFragment : BottomSheetDialogFragment() {

    @BindView(R.id.et_player_name) lateinit var etPlayerName: EditText
    @BindView(R.id.btn_add_new_player) lateinit var btnAddNewPlayer: ImageButton

    private lateinit var listener: NewPlayerDialogListener
    private lateinit var unbinder: Unbinder

    interface NewPlayerDialogListener {
        fun onFinishEditDialog(inputName: String)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = activity as NewPlayerDialogListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, bundle: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_add_player, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
    }

    @OnClick(R.id.btn_add_new_player)
    fun onAddNewPlayerClick() {
        passNameToActivity()
    }

    @OnEditorAction(R.id.et_player_name)
    fun onEditorAction(actionId: Int): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            passNameToActivity()
            return true
        }
        return false
    }

    private fun passNameToActivity() {
        val name = etPlayerName.text.toString()
        if (!name.isEmpty()) {
            listener.onFinishEditDialog(name)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}

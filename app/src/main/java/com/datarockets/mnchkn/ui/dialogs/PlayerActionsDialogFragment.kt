package com.datarockets.mnchkn.ui.dialogs

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.ui.base.BaseActivity
import javax.inject.Inject

class PlayerActionsDialogFragment : BottomSheetDialogFragment() {

    @BindView(R.id.lv_player_actions) lateinit var lvPlayerActions: ListView

    @Inject lateinit var mPlayerEditorActionsAdapter: PlayerEditorActionsAdapter

    private lateinit var mUnbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_player_action, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUnbinder = ButterKnife.bind(this, view!!)
        lvPlayerActions.adapter = mPlayerEditorActionsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }


}
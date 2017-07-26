package com.datarockets.mnchkn.ui.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.models.Dice
import com.datarockets.mnchkn.ui.base.BaseActivity
import javax.inject.Inject

class RollDiceDialogFragment : DialogFragment() {

    @BindView(R.id.tv_dice_value) lateinit var tvDiceValue: TextView

    @Inject lateinit var dice: Dice

    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_roll_dice, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
        tvDiceValue.text = dice.roll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}

package com.datarockets.mnchkn.ui.player

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.ui.base.BaseActivity
import com.datarockets.mnchkn.views.fonts.MunchkinTextView
import javax.inject.Inject

class PlayerFragment : Fragment(), PlayerView {

    @BindView(R.id.btn_level_score_up) lateinit var btnLevelScoreUp: ImageButton
    @BindView(R.id.btn_level_score_down) lateinit var btnLevelScoreDown: ImageButton
    @BindView(R.id.btn_strength_score_up) lateinit var btnStrengthScoreUp: ImageButton
    @BindView(R.id.btn_strength_score_down) lateinit var btnStrengthScoreDown: ImageButton
    @BindView(R.id.tv_player_name) lateinit var tvPlayerName: MunchkinTextView
    @BindView(R.id.tv_level_score) lateinit var tvLevelScore: MunchkinTextView
    @BindView(R.id.tv_strength_score) lateinit var tvStrengthScore: MunchkinTextView

    @Inject lateinit var presenter: PlayerPresenter

    private lateinit var unbinder: Unbinder

    private var listener: OnScoresChangedListener? = null
    private var playerPosition = 0

    interface OnScoresChangedListener {
        fun onScoresChanged(playerPosition: Int, playerLevel: Int, strengthScore: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = (activity as OnScoresChangedListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, bundle: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
        presenter.attachView(this)
    }

    @OnClick(R.id.btn_level_score_up)
    fun onIncreaseLevelButtonClick() {
        presenter.increaseLevelScore()
    }

    @OnClick(R.id.btn_level_score_down)
    fun onDecreaseLevelButtonClick() {
        presenter.decreaseLevelScore()
    }

    @OnClick(R.id.btn_strength_score_up)
    fun onIncreaseStrengthButtonClick() {
        presenter.increaseStrengthScore()
    }

    @OnClick(R.id.btn_strength_score_down)
    fun onDecreaseStrengthButtonClick() {
        presenter.decreaseStrengthScore()
    }

    override fun showPlayerName(playerName: String) {
        tvPlayerName.text = playerName
    }

    override fun showPlayerScores(levelScore: String, strengthScore: String) {
        tvLevelScore.text = levelScore
        tvStrengthScore.text = strengthScore
        listener?.onScoresChanged(playerPosition, levelScore.toInt(), strengthScore.toInt())
    }

    fun loadPlayerScores(playerId: Long, playerPosition: Int) {
        presenter.loadPlayerScores(playerId)
        this.playerPosition = playerPosition
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
        unbinder.unbind()
    }
}

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
import com.datarockets.mnchkn.data.models.Player
import com.datarockets.mnchkn.ui.base.BaseActivity
import com.datarockets.mnchkn.views.fonts.MunchkinTextView
import javax.inject.Inject

class PlayerFragment : Fragment(), PlayerView {

    private val mPlayerId: Int = 0
    private val mPosition: Int = 0

    @BindView(R.id.btn_level_score_up) lateinit var btnLevelScoreUp: ImageButton
    @BindView(R.id.btn_level_score_down) lateinit var btnLevelScoreDown: ImageButton
    @BindView(R.id.btn_strength_score_up) lateinit var btnStrengthScoreUp: ImageButton
    @BindView(R.id.btn_strength_score_down) lateinit var btnStrengthScoreDown: ImageButton
    @BindView(R.id.tv_player_name) lateinit var tvPlayerName: MunchkinTextView
    @BindView(R.id.tv_level_score) lateinit var tvLevelScore: MunchkinTextView
    @BindView(R.id.tv_strength_score) lateinit var tvStrengthScore: MunchkinTextView

    @Inject lateinit var presenter: PlayerPresenter

    private lateinit var callback: PlayerFragmentCallback
    private lateinit var unbinder: Unbinder

    interface PlayerFragmentCallback {
        fun onScoreChanged(player: Player, index: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as BaseActivity).activityComponent().inject(this)
        //        mPlayerId = getArguments().getLong(PLAYER_ID, -1);
        //        mPosition = getArguments().getInt(POSITION, 0);
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = activity as PlayerFragmentCallback
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, bundle: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view!!)
        presenter.attachView(this)
        presenter.loadPlayerScores(mPlayerId)
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

    override fun showPlayerScores(levelScore: Int, strengthScore: Int) {
        tvLevelScore.text = levelScore.toString()
        tvStrengthScore.text = strengthScore.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
        unbinder.unbind()
    }

    companion object {

        private val PLAYER_ID = "player_id"
        private val POSITION = "position"

        fun newInstance(playerId: Long, position: Int): PlayerFragment {
            val args = Bundle()
            args.putLong(PLAYER_ID, playerId)
            args.putInt(POSITION, position)
            val fragment = PlayerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

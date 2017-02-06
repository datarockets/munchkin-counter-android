package com.datarockets.mnchkn.ui.share

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.datarockets.mnchkn.R
import com.datarockets.mnchkn.data.local.SharingHelper.SHARE_WITH
import javax.inject.Inject

class ShareResultDialogFragment: BottomSheetDialogFragment(), ShareResultView {

    @BindView(R.id.iv_share_facebook) lateinit var ivShareFacebook: ImageView
    @BindView(R.id.iv_share_twitter) lateinit var ivShareTwitter: ImageView
    @BindView(R.id.iv_share_email) lateinit var ivShareEmail: ImageView
    @BindView(R.id.iv_share_etc) lateinit var ivShareEtc: ImageView

    @Inject lateinit var shareResultPresenter: ShareResultPresenter

    private lateinit var mUnbinder: Unbinder

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_share_result, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mUnbinder = ButterKnife.bind(this, view!!)
        shareResultPresenter.attachView(this)
    }

    @OnClick(R.id.iv_share_facebook)
    fun onShareFacebookClick() {
        shareResultPresenter.shareGameResults(SHARE_WITH.FACEBOOK)
    }

    @OnClick(R.id.iv_share_twitter)
    fun onShareTwitterClick() {
        shareResultPresenter.shareGameResults(SHARE_WITH.TWITTER)
    }

    @OnClick(R.id.iv_share_email)
    fun onShareEmailClick() {
        shareResultPresenter.shareGameResults(SHARE_WITH.GMAIL)
    }

    @OnClick(R.id.iv_share_etc)
    fun onShareEtcClick() {
        shareResultPresenter.shareGameResults(SHARE_WITH.LINK)
    }

    override fun launchShareableIntent(generatedIntent: Intent) {
        startActivity(generatedIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
        shareResultPresenter.detachView()
    }

}

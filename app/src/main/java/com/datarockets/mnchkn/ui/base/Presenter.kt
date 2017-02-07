package com.datarockets.mnchkn.ui.base


interface Presenter<in T : MvpView> {
    fun attachView(mvpView: T)
    fun detachView()
}

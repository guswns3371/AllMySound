package com.example.allmysound.Music.PlayList

import android.content.Context

class PlayListPresenter(private var context: Context?):PlayListContract.Presenter {

    private var view: PlayListContract.View?=null

    override fun setView(view: PlayListContract.View) {
        this.view = view
    }
    override fun releaseView() {
    }
}
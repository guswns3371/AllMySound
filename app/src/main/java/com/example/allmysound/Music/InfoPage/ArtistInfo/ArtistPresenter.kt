package com.example.allmysound.Music.InfoPage.ArtistInfo

import android.content.Context

class ArtistPresenter(private var context: Context?) :ArtistContract.Presenter{

    private lateinit var view: ArtistContract.View
    override fun setView(view: ArtistContract.View) {
        this.view =view
    }

    override fun releaseView() {
    }
}
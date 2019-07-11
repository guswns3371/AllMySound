package com.example.allmysound.Music.InfoPage.ArtistInfo

import android.content.Context
import com.example.allmysound.Base.BaseContract
import com.example.allmysound.Main.Model.SongInfo

interface ArtistContract {
    interface View: BaseContract.View{
        override fun showToast(message: String)
    }
    interface Presenter: BaseContract.Presenter<View>{
        override fun setView(view: View)
        override fun releaseView()
    }
}
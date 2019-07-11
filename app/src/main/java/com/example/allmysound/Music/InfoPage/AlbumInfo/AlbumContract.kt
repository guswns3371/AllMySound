package com.example.allmysound.Music.InfoPage.AlbumInfo

import android.content.Context
import com.example.allmysound.Base.BaseContract
import com.example.allmysound.Main.Model.SongInfo

interface AlbumContract {
    interface View:BaseContract.View{
        override fun showToast(message: String)
        fun getFragIntent(): String?
    }
    interface Presenter:BaseContract.Presenter<View>{
        override fun setView(view: View)
        override fun releaseView()
    }
}
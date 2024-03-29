package com.example.allmysound.Music.SongList

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.allmysound.Main.Model.SongInfo
import java.io.FileNotFoundException
import java.io.IOException

class SongListPresenter(private var context: Context?):SongListContract.Presenter {

    private var view: SongListContract.View?=null

    override fun setView(view: SongListContract.View) {
        this.view = view
    }

    override fun releaseView() {
    }

}
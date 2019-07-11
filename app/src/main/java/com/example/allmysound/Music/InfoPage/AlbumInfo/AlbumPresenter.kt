package com.example.allmysound.Music.InfoPage.AlbumInfo

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.allmysound.Main.Model.SongInfo
import java.io.FileNotFoundException
import java.io.IOException

class AlbumPresenter(private var context: Context?): AlbumContract.Presenter {

    private lateinit var view : AlbumContract.View

    override fun setView(view: AlbumContract.View) {
        this.view = view
    }

    override fun releaseView() {
    }

}
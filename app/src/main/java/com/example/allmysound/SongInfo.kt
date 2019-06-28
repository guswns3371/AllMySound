package com.example.allmysound

import android.graphics.Bitmap
import android.net.Uri

class SongInfo{
     var idx: String
     var img: String
     var artist: String
     var title: String
     var album: String
     var time: String
     var file_path: String
     var file_name: String

    constructor(
        idx: String, img: String, artist: String, title: String,
        album: String,time: String, file_path: String, file_name: String) {
        this.idx = idx
        this.img = img
        this.artist = artist
        this.title = title
        this.album = album
        this.time = time
        this.file_path = file_path
        this.file_name = file_name
    }

    override fun toString(): String {
        return "SongInfo(idx='$idx', \nimg=$img, \nartist='$artist', \ntitle='$title', \nalbum='$album', time='$time', \nfile_path='$file_path', \nfile_name='$file_name')"
    }


}
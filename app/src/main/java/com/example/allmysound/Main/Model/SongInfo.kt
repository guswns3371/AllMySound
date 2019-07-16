package com.example.allmysound.Main.Model

import java.io.Serializable

class SongInfo(
    var orderNum: Int,
    var idx: String,
    var img: String,
    var artist: String,
    var albumArtist: String,
    var title: String,
    var album: String,
    var album_track_num: String,
    var time: String,
    var genre: String,
    var date: String,
    var file_path: String,
    var file_name: String,
    var file_modified: String
) : Serializable {

    override fun toString(): String {
        return "SongInfo(orderNum='$orderNum', \nidx='$idx', \nimg=$img, \nartist='$artist', " +
                "\ntitle='$title', \nalbum='$album', \nalbumArtist='$albumArtist', \nalbum_track_num='$album_track_num', " +
                "\ntime='$time', \ngenre='$genre', \ndate='$date', \nfile_path='$file_path', " +
                "\nfile_name='$file_name', \nfile_modified='$file_modified',)"
    }


}
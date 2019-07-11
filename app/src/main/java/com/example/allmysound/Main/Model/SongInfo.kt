package com.example.allmysound.Main.Model

class SongInfo{
     var orderNum:Int
     var idx: String
     var img: String
     var artist: String
     var title: String
     var album: String
     var album_track_num: String
     var time: String
     var file_path: String
     var file_name: String

    constructor(
        orderNum:Int,
        idx: String, img: String, artist: String, title: String,
        album: String,album_track_num: String, time: String, file_path: String, file_name: String) {
        this.orderNum = orderNum
        this.idx = idx
        this.img = img
        this.artist = artist
        this.title = title
        this.album = album
        this.album_track_num = album_track_num
        this.time = time
        this.file_path = file_path
        this.file_name = file_name
    }

    override fun toString(): String {
        return "SongInfo(orderNum='$orderNum', \nidx='$idx', \nimg=$img, \nartist='$artist', \ntitle='$title', \nalbum='$album', \nalbum_track_num='$album_track_num', time='$time', \nfile_path='$file_path', \nfile_name='$file_name')"
    }


}
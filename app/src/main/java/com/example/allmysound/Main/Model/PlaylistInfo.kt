package com.example.allmysound.Main.Model

import java.io.Serializable

class PlaylistInfo(
    var idx:String,
    var img:String,
    var title:String,
    var count:String
):Serializable {

    override fun toString(): String {
        return "PlaylistInfo(idx='$idx', img='$img', title='$title', count='$count')"
    }
}
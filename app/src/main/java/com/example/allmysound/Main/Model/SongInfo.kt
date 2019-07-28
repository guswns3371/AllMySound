package com.example.allmysound.Main.Model

import android.os.Parcel
import android.os.Parcelable
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
) : Serializable, Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun toString(): String {
        return "SongInfo(orderNum='$orderNum', \nidx='$idx', \nimg=$img, \nartist='$artist', " +
                "\ntitle='$title', \nalbum='$album', \nalbumArtist='$albumArtist', \nalbum_track_num='$album_track_num', " +
                "\ntime='$time', \ngenre='$genre', \ndate='$date', \nfile_path='$file_path', " +
                "\nfile_name='$file_name', \nfile_modified='$file_modified',)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(orderNum)
        parcel.writeString(idx)
        parcel.writeString(img)
        parcel.writeString(artist)
        parcel.writeString(albumArtist)
        parcel.writeString(title)
        parcel.writeString(album)
        parcel.writeString(album_track_num)
        parcel.writeString(time)
        parcel.writeString(genre)
        parcel.writeString(date)
        parcel.writeString(file_path)
        parcel.writeString(file_name)
        parcel.writeString(file_modified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SongInfo> {
        override fun createFromParcel(parcel: Parcel): SongInfo {
            return SongInfo(parcel)
        }

        override fun newArray(size: Int): Array<SongInfo?> {
            return arrayOfNulls(size)
        }
    }


}
package com.example.allmysound.Base

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.allmysound.Main.Model.SongInfo
import java.io.FileNotFoundException
import java.io.IOException

interface BaseContract {
    interface View{
        fun showToast(message:String)
    }
    interface Presenter<T>{
        fun setView(view: T)
        fun releaseView()
        @SuppressLint("Recycle")
        fun loadDataByQuery(context: Context, query : String, orderBy : String) : ArrayList<SongInfo>{

                val columnIndexIdx : Int
                val columnIndexData : Int
                val columnIndexName: Int
                val columnIndexDataAlbumId: Int
                val columnIndexDataAlbum: Int
                val columnIndexDataAlbumTrackNum: Int
                val columnIndexDataArtist: Int
                val columnIndexDataTitle: Int
                val columnIndexDataTime: Int

                val listOfAllSongs : ArrayList<SongInfo> = ArrayList()

                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val projection  =arrayOf(
                    MediaStore.MediaColumns.DATA,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ALBUM_ID,
                    MediaStore.Audio.Albums._ID,
                    MediaStore.Audio.AudioColumns.ALBUM,
                    MediaStore.Audio.AudioColumns.ALBUM_ID,
                    MediaStore.Audio.AudioColumns.ARTIST,
                    MediaStore.Audio.AudioColumns._ID,
                    MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                    MediaStore.Audio.AudioColumns.TITLE,
                    MediaStore.Audio.AudioColumns.TITLE_KEY,
                    MediaStore.Audio.AudioColumns.DURATION,
                    MediaStore.Audio.AudioColumns.TRACK
                )
                val orderByStr = "$orderBy ASC"
                val cursor =context.contentResolver.query(uri, projection, query,null, orderByStr)

                columnIndexName = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
                columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                columnIndexDataTime=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.DURATION)
                columnIndexDataAlbum=   cursor.getColumnIndexOrThrow( MediaStore.Audio.Media.ALBUM)
                columnIndexDataAlbumTrackNum=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.TRACK)
                columnIndexDataAlbumId=   cursor.getColumnIndexOrThrow( MediaStore.Audio.Media.ALBUM_ID)
                columnIndexDataArtist=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.ARTIST)
                columnIndexDataTitle=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.TITLE)
                columnIndexIdx=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns._ID)

                var num = 0
                while (cursor.moveToNext()){
                    val idx = cursor.getString(columnIndexIdx)
                    val artist = cursor.getString(columnIndexDataArtist)
                    val title = cursor.getString(columnIndexDataTitle)
                    val path = cursor.getString(columnIndexData)
                    val filename = cursor.getString(columnIndexName)
                    val album = cursor.getString(columnIndexDataAlbum)
                    val albumTrackNum = cursor.getString(columnIndexDataAlbumTrackNum)
                    val albumId = cursor.getLong(columnIndexDataAlbumId)
                    var time = cursor.getString(columnIndexDataTime)
                    var imgUri: Uri? = null
                    try {
                        val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
                        imgUri = ContentUris.withAppendedId(sArtworkUri, albumId)
                    }catch (e : FileNotFoundException){
                        Log.e("Artwork Exception1","$e")
                    }catch (e : IOException){
                        Log.e("Artwork Exception2","$e")
                    }
                    time = if(time==null) "0" else time
                    listOfAllSongs.add(
                        SongInfo(
                            num,
                            idx,
                            imgUri.toString(),
                            artist,
                            title,
                            album,
                            albumTrackNum,
                            time,
                            path,
                            filename
                        )
                    )
                    num++
                    //uri 를 저장해버리면 => json으로 변환시 uri 부분이 변환되지 않는다
                }

                return listOfAllSongs

        }
    }
}
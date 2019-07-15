package com.example.allmysound.Base

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.allmysound.Main.Model.SongInfo
import java.io.FileNotFoundException
import java.io.IOException
import java.util.concurrent.TimeUnit

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
                val columnIndexDataModified : Int
                val columnIndexName: Int
                var columnIndexGenre: Int
                var columnIndexAlbumArtist: Int
                val columnIndexDate: Int
                val columnIndexDataAlbumId: Int
                val columnIndexDataAlbum: Int
                val columnIndexDataAlbumTrackNum: Int
                val columnIndexDataArtist: Int
                val columnIndexDataTitle: Int
                val columnIndexDataTime: Int

                val listOfAllSongs : ArrayList<SongInfo> = ArrayList()

                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val mediaProjection  =arrayOf(
                    MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.DATE_MODIFIED,
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
                    MediaStore.Audio.AudioColumns.TRACK,
                    MediaStore.Audio.AudioColumns.YEAR,
                    "album_artist"
                )
            val genreProjection = arrayOf(
                MediaStore.Audio.Genres.NAME,
                MediaStore.Audio.Genres._ID
            )

                val mediaCursor =context.contentResolver.query(uri, mediaProjection, query,null, orderBy)

                columnIndexName = mediaCursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
                columnIndexData = mediaCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                columnIndexDataModified = mediaCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)
                columnIndexDataTime=   mediaCursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.DURATION)
                columnIndexDataAlbum=   mediaCursor.getColumnIndexOrThrow( MediaStore.Audio.Media.ALBUM)
                columnIndexDataAlbumTrackNum=   mediaCursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.TRACK)
                columnIndexDataAlbumId=   mediaCursor.getColumnIndexOrThrow( MediaStore.Audio.Media.ALBUM_ID)
                columnIndexDataArtist=   mediaCursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.ARTIST)
                columnIndexDataTitle=   mediaCursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.TITLE)
                columnIndexIdx=   mediaCursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns._ID)
                columnIndexDate=   mediaCursor.getColumnIndexOrThrow(  MediaStore.Audio.AudioColumns.YEAR)
                columnIndexAlbumArtist=   mediaCursor.getColumnIndexOrThrow(  "album_artist")

                var num = 0
            if (mediaCursor.moveToFirst()) {
                do{
                    val idx = mediaCursor.getString(columnIndexIdx)
                    val artist = mediaCursor.getString(columnIndexDataArtist)
                    val title = mediaCursor.getString(columnIndexDataTitle)
                    val path = mediaCursor.getString(columnIndexData)
                    val filename = mediaCursor.getString(columnIndexName)
                    var modifiedDate = mediaCursor.getString(columnIndexDataModified)
                    val album = mediaCursor.getString(columnIndexDataAlbum)
                    val albumTrackNum = mediaCursor.getString(columnIndexDataAlbumTrackNum)
                    val albumId = mediaCursor.getLong(columnIndexDataAlbumId)
                    var time = mediaCursor.getString(columnIndexDataTime)
                    var date = mediaCursor.getString(columnIndexDate)
                    var albumArtist = mediaCursor.getString(columnIndexAlbumArtist)

                    var genre  =""
//                    val genUri = MediaStore.Audio.Genres.getContentUriForAudioId("external", idx.toInt())
//                    val  genresCursor = context.contentResolver.query(genUri, genreProjection, null, null, null)
//                    columnIndexGenre = genresCursor!!.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME)
//                    if  (genresCursor.moveToFirst())
//                        genre = genresCursor.getString(columnIndexGenre)


                    var imgUri: Uri? = null
                    try {
                        val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
                        imgUri = ContentUris.withAppendedId(sArtworkUri, albumId)
                    }catch (e : FileNotFoundException){
                        Log.e("Artwork Exception1","$e")
                    }catch (e : IOException){
                        Log.e("Artwork Exception2","$e")
                    }

                    time = if(time==null) "0" else {
                        val timeLong = time.toLong()
                        val min = TimeUnit.MILLISECONDS.toMinutes(timeLong)
                        val sec = TimeUnit.MILLISECONDS.toSeconds(timeLong) - min*60
                        val secStr = if (sec<10)  "0$sec" else  "$sec"
                        "$min:$secStr"
                    }
                    date = date ?: "Unknown"
                    albumArtist = albumArtist ?: "Unknown"
                    modifiedDate = modifiedDate ?: "Unknown"

                    listOfAllSongs.add(
                        SongInfo(
                            num,
                            idx,
                            imgUri.toString(),
                            artist,
                            albumArtist,
                            title,
                            album,
                            albumTrackNum,
                            time,
                            genre,
                            date,
                            path,
                            filename,
                            modifiedDate
                        )
                    )
                    num++
                    //uri 를 저장해버리면 => json으로 변환시 uri 부분이 변환되지 않는다
                }while (mediaCursor.moveToNext())
            }


                return listOfAllSongs
        }
    }
}
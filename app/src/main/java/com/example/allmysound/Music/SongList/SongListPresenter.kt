package com.example.allmysound.Music.SongList

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.allmysound.SongInfo
import java.io.FileNotFoundException
import java.io.IOException

class SongListPresenter(private var context: Context?):SongListContract.Presenter {

    private var view: SongListContract.View?=null

    override fun setView(view: SongListContract.View) {
        this.view = view
    }

    override fun releaseView() {
    }

    @SuppressLint("Recycle")
    override fun loadSong(): ArrayList<SongInfo> {

        val column_index_idx : Int
        val column_index_data : Int
        val column_index_name: Int
        val column_index_data_albumid: Int
        val column_index_data_album: Int
        val column_index_data_artist: Int
        val column_index_data_title: Int
        val column_index_data_time: Int

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
            MediaStore.Audio.AudioColumns.DURATION
        )
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val orderBy = MediaStore.Audio.AudioColumns.DISPLAY_NAME + " ASC"
        val cursor =context!!.contentResolver.query(uri, projection, selection,null, orderBy)

        column_index_name = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_index_data_time=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.DURATION)
        column_index_data_album=   cursor.getColumnIndexOrThrow( MediaStore.Audio.Media.ALBUM)
        column_index_data_albumid=   cursor.getColumnIndexOrThrow( MediaStore.Audio.Media.ALBUM_ID)
        column_index_data_artist=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.ARTIST)
        column_index_data_title=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns.TITLE)
        column_index_idx=   cursor.getColumnIndexOrThrow( MediaStore.Audio.AudioColumns._ID)

        while (cursor.moveToNext()){
            val idx = cursor.getString(column_index_idx)
            val artist = cursor.getString(column_index_data_artist)
            val title = cursor.getString(column_index_data_title)
            val path = cursor.getString(column_index_data)
            val filename = cursor.getString(column_index_name)
            val album = cursor.getString(column_index_data_album)
            val albumId = cursor.getLong(column_index_data_albumid)
            val time = cursor.getString(column_index_data_time)
            var imgUri: Uri? = null
            try {
                val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
                imgUri = ContentUris.withAppendedId(sArtworkUri, albumId)
            }catch (e : FileNotFoundException){
                Log.e("Artwork Exception1","$e")
            }catch (e : IOException){
                Log.e("Artwork Exception2","$e")
            }
            listOfAllSongs.add(SongInfo(idx,imgUri.toString(),artist,title,album,time,path,filename))
            //uri 를 저장해버리면 => json으로 변환시 uri 부분이 변환되지 않는다
        }

        return listOfAllSongs
    }
}
package com.example.allmysound.Main.Pref

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.example.allmysound.Main.Model.SongInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

private const val PRE_FILENAME = "prefs_"
private const val PLAY = "Play"
private const val SHUFFLE = "Shuffle"
private const val ROTATE = "Rotate"
private const val LIKE = "Like"
private const val SONGINFO = "IsPlayingNow"
private const val PLAYLIST = "Playlist"
private const val ALLSONGLIST = "Allsonglist"

class MySharedPreference(private var context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PRE_FILENAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()
    private val gson = GsonBuilder().create()
    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

    fun setPlayBoolean(value : Boolean) {
        editor.putBoolean(PLAY,value).apply()
    }
    fun getPlayBoolean():Boolean{
        return prefs.getBoolean(PLAY,false)
    }
    fun setShuffleBoolean(value : Boolean) {
        editor.putBoolean(SHUFFLE,value).apply()
    }
    fun getShuffleBoolean():Boolean{
        return prefs.getBoolean(SHUFFLE,false)
    }
    fun setRotateBoolean(value : Boolean) {
        editor.putBoolean(ROTATE,value).apply()
    }
    fun getRotateBoolean():Boolean{
        return prefs.getBoolean(ROTATE,false)
    }
    fun setLikeBoolean(value : Boolean) {
        editor.putBoolean(LIKE,value).apply()
    }
    fun getLikeBoolean():Boolean{
        return prefs.getBoolean(LIKE,false)
    }
    fun setIsPlayingInfo(value : SongInfo) {
        val strSongInfo = gson.toJson(value)
        editor.putString(SONGINFO,strSongInfo).apply()
    }
    fun getIsPlayingInfo(): SongInfo?{
        val str = prefs.getString(SONGINFO,null)
        return if (str == null) null else Gson().fromJson(str)
    }
    fun setPlayListInt(playlist: ArrayList<Int>){
        val mIntList   = playlist.toArray()!!
        editor.putString(PLAYLIST,TextUtils.join(",=,",mIntList)).apply()
    }
    fun getPlayListInt():ArrayList<Int>?{
        val mList = TextUtils.split(prefs.getString(PLAYLIST,""),",=,")
        val arrayToList = ArrayList<String>(mList.toList())
        val newList = ArrayList<Int>()

        arrayToList.forEach {
            newList.add(it.toInt())
        }
        return  newList
    }

}
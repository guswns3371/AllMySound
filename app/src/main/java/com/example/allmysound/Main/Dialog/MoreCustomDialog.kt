package com.example.allmysound.Main.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmysound.Main.Dialog.Adapter.CPlayListAdapter
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.music_more_custom.*

class MoreCustomDialog(context: Context) : Dialog(context) {
    interface ClickListener{
        fun clickInfo()
        fun clickPlaylist()
        fun clickPlaylistAdd()
        fun clickDelete()
        fun clickCancel()
        fun clickShuffle()
        fun clickUnshuffle()
    }

    interface SetData {
        fun setImage() : String
        fun setTitle() : String
        fun setArtist() : String
        fun setAlbum() : String
    }
    var mClickListener: ClickListener? =null
    var mSetData: SetData? =null
    private lateinit var songlist : ArrayList<SongInfo>
    private lateinit var numlist: ArrayList<Int>
    private  var orderNum: Int? = null
    private var myCPlayListAdapter : CPlayListAdapter? =null
    fun setSongList(songlist : ArrayList<SongInfo>){
        this.songlist = songlist
    }
    fun setNumList(numlist : ArrayList<Int>){
        this.numlist = numlist
    }
    fun setPlayingIdx(orderNum: Int){
        this.orderNum = orderNum
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setting()
        setListener()
        setDatas()
        textScrolling()
        setPlayListRV()
    }
    private fun setting(){
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.8f
        window?.attributes = layoutParams

        setContentView(R.layout.music_more_custom)
    }
    private fun setListener(){
        if (mClickListener !=null){
            more_info.setOnClickListener { mClickListener!!.clickInfo() }
            more_playlist.setOnClickListener { mClickListener!!.clickPlaylist() }
            more_playlist_add.setOnClickListener { mClickListener!!.clickPlaylistAdd() }
            more_delete.setOnClickListener { mClickListener!!.clickDelete() }
            more_cancel_btn.setOnClickListener { mClickListener!!.clickCancel() }
            cshuffle_btn.setOnClickListener {mClickListener!!.clickShuffle() ; setPlayListRV()}
            cunshuffle_btn.setOnClickListener { mClickListener!!.clickUnshuffle() ; setPlayListRV() }
        }
    }
    private fun setDatas(){
        if (mSetData !=null){
            more_title.text = mSetData!!.setTitle()
            more_artist.text = mSetData!!.setArtist()
            more_album.text = mSetData!!.setAlbum()
            Picasso.get()
                .load(mSetData!!.setImage().toUri())
                .error(R.drawable.song_500)
                .into(more_img)
        }
    }
    private fun textScrolling(){
        more_title.isSelected =true
        more_artist.isSelected =true
        more_album.isSelected =true
    }
    private fun setPlayListRV(){
        myCPlayListAdapter = CPlayListAdapter(context,songlist,numlist,orderNum!!)
        myCPlayListAdapter!!.mClickListener = object : CPlayListAdapter.CustomPlayListClickListener{
            override fun onClick(pos: Int) {
            }
        }
        playlist_RV.adapter = myCPlayListAdapter
        playlist_RV.layoutManager= LinearLayoutManager(context)
        playlist_RV.setHasFixedSize(true)
        Log.e("setPlayList ", "${numlist.indexOf(orderNum!!)}")
        playlist_RV.scrollToPosition(numlist.indexOf(orderNum!!))
    }
    private fun setExistingPlayListRV(){

    }

    fun showPlayList(){
        more_setting.visibility = View.GONE
        playlist_LO.visibility = View.VISIBLE
    }
   private fun hidePlayList(){
        more_setting.visibility = View.VISIBLE
       playlist_LO.visibility = View.GONE
    }
    fun showExistingPlayList(){
        more_setting.visibility = View.GONE
        existing_RV.visibility = View.VISIBLE

    }
   private fun hideExistingPlayList(){
        more_setting.visibility = View.VISIBLE
        existing_RV.visibility = View.GONE
    }

    override fun onBackPressed() {
        if(playlist_RV.visibility==View.VISIBLE)
            hidePlayList()

        if(existing_RV.visibility==View.VISIBLE)
            hideExistingPlayList()

    }
}
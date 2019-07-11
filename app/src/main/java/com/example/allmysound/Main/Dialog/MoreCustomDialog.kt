package com.example.allmysound.Main.Dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allmysound.Main.Dialog.Adapter.CPlayListAdapter
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.music_more_custom.*
import java.util.*
import kotlin.collections.ArrayList

class MoreCustomDialog(context: Context) : Dialog(context) {
    interface ClickListener{
        fun clickInfo()
        fun clickPlaylist()
        fun clickPlaylistAdd()
        fun clickArtistInfo()
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
    private var myCPlayListAdapter : CPlayListAdapter? =null
    fun setSongList(songlist : ArrayList<SongInfo>){
        this.songlist = songlist
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setting()
        setListener()
        setDatas()
        textScrolling()
        setPlayListRV()

        initTouchHelper()
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
            more_artistinfo.setOnClickListener { mClickListener!!.clickArtistInfo() }
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
        myCPlayListAdapter = CPlayListAdapter(context,songlist)
        myCPlayListAdapter!!.mClickListener = object : CPlayListAdapter.CustomPlayListClickListener{
            override fun onItemClick(pos: Int) {
                MainActivity.createMainPresenter().PlaylistllinkData(songlist)
                MainActivity.createMainPresenter().PlaylistllinkDataIndex(pos)
                MainActivity.createMainPresenter().checkIsPlaying()
            }
        }
        MainActivity.createMainPresenter().PlaylistllinkData(songlist)
        MainActivity.createMainPresenter().PlaylistlinkAdapter(myCPlayListAdapter!!)

        playlist_RV.adapter = myCPlayListAdapter
        playlist_RV.layoutManager= LinearLayoutManager(context)
        playlist_RV.setHasFixedSize(true)
//     https://www.youtube.com/watch?v=dldrLPNoFnk

        val orderNum = MainActivity.prefs.getIsPlayingInfo()!!.orderNum
        val numlist = MainActivity.prefs.getPlayListInt()
        Log.e("setPlayList ", "${numlist.indexOf(orderNum)}")
        playlist_RV.scrollToPosition(numlist.indexOf(orderNum))
    }
    private fun setExistingPlayListRV(){

    }

    private fun initTouchHelper(){
        val touchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback
            (ItemTouchHelper.UP or ItemTouchHelper.DOWN,0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePos = viewHolder.adapterPosition
                val targetPos = target.adapterPosition
                myCPlayListAdapter!!.onPositionMoved(sourcePos, targetPos)
                return true
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//                super.clearView(recyclerView, viewHolder)
                myCPlayListAdapter!!.notifyDataSetChanged() // 유저가 drop할 때 notifydatasetchanged를 해준다.
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })
        touchHelper.attachToRecyclerView(playlist_RV)
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

        if(more_setting.visibility==View.VISIBLE)
           super.onBackPressed()
    }

    private fun showLog(message: Any) {
        Log.e("MoreCustomDialog  ","$message")
//        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }
}
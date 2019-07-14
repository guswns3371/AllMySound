package com.example.allmysound.Main.Dialog.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.R
import kotlin.collections.ArrayList


class CPlayListAdapter (
    private val context: Context,
    private var songlist: ArrayList<SongInfo>
): RecyclerView.Adapter<CPlayListAdapter.MyViewHolder>()  {

    interface CustomPlayListClickListener{
        fun onItemClick(pos:Int)
    }
    var mClickListener: CustomPlayListClickListener? =null
    private val numlist: ArrayList<Int> = MainActivity.prefs.getPlayListInt()

    inner class MyViewHolder (itemView : View?) : RecyclerView.ViewHolder(itemView!!) {
        val csongunderline = itemView?.findViewById<LinearLayout>(R.id.csong_underline)
        val csongimg = itemView?.findViewById<ImageView>(R.id.csong_img)
        val cartistname = itemView?.findViewById<TextView>(R.id.csong_artist)
        val csongname = itemView?.findViewById<TextView>(R.id.csong_title)
        val cplaying_img = itemView?.findViewById<ImageView>(R.id.cplaying_now_img)
        val cmoving_playlist = itemView?.findViewById<ImageView>(R.id.cmoving_playlist)
        @SuppressLint("ResourceAsColor")
        fun bind(song: SongInfo, pos : Int, context: Context) {

            this.csongimg?.let {
                Glide.with(context)
                    .load(song.img.toUri())
                    .apply(RequestOptions().error(R.drawable.song_500))
                    .into(it)
            }
//            val text  =
//                "$pos :: ${numlist[pos]}/" +
//                        "${numlist.indexOf(orderNum)}/"+
//                        "${songlist[numlist[pos]].orderNum}/"+
//                        "$orderNum"
//            cartistname?.text =text
            cartistname?.text = song.artist
            csongname?.text = song.title

            val orderNum= MainActivity.prefs.getIsPlayingInfo()!!.orderNum
            if(numlist.indexOf(orderNum) == pos){
                csongname?.typeface = Typeface.DEFAULT_BOLD
                cplaying_img?.visibility = View.VISIBLE
                csongunderline?.visibility = View.VISIBLE
                cmoving_playlist?.visibility = View.GONE
            } else{
                csongname?.typeface = Typeface.DEFAULT
                cplaying_img?.visibility = View.GONE
                csongunderline?.visibility = View.INVISIBLE
                cmoving_playlist?.visibility = View.VISIBLE
            }

            if(mClickListener!=null){
                itemView.setOnClickListener{
                    mClickListener?.onItemClick(pos)
                    MainActivity.prefs.setIsPlayingInfo(song)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.cplaylist_item,p0,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return numlist.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.bind(songlist[numlist[p1]],p1,context)
    }

    private fun removeItem(position: Int) {
        songlist.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, songlist.size)
    }

    fun onPositionMoved(oldPos: Int, newPos: Int){
        val oldNum = numlist[oldPos]
        numlist.remove(oldNum)
        numlist.add(newPos,oldNum)
        MainActivity.prefs.setPlayListInt(numlist)

        if(MainActivity.prefs.getShuffleBoolean())
            MainActivity.presenter.PlaylistllinkDataUpdateIndex(
            numlist.indexOf(MainActivity.prefs.getIsPlayingInfo()!!.orderNum)) // randomIdx를 업데이트 한다
        else
            MainActivity.presenter.SonglinkDataUpdateIndex(
                numlist.indexOf(MainActivity.prefs.getIsPlayingInfo()!!.orderNum)) // Idx를 업데이트 한다

        notifyItemMoved(oldPos,newPos)
    }

    fun onPositionSwiped(pos:Int){
        songlist.removeAt(numlist[pos])
        notifyItemRemoved(pos)
    }
}
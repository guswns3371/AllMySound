package com.example.allmysound.Music.SongList.Adapter

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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.allmysound.Base.Extensions.getPreference
import com.example.allmysound.R
import com.example.allmysound.Main.Model.SongInfo

class SongListAdapter (
    private val context: Context,
    private var songlist: ArrayList<SongInfo>
): androidx.recyclerview.widget.RecyclerView.Adapter<SongListAdapter.MyViewHolder>()  {

    interface SongListClickListener{
        fun onClick(pos:Int)
    }
    var mClickListener: SongListClickListener? =null

    inner class MyViewHolder (itemView : View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        private val songunderline = itemView?.findViewById<LinearLayout>(R.id.song_underline)
        private val songimg = itemView?.findViewById<ImageView>(R.id.song_img)
        private val artistname = itemView?.findViewById<TextView>(R.id.song_artist)
        private val songname = itemView?.findViewById<TextView>(R.id.song_title)
        private val playing_img = itemView?.findViewById<ImageView>(R.id.playing_now_img)
        @SuppressLint("ResourceAsColor")
        fun bind(song: SongInfo, pos : Int, context: Context) {

            this.songimg?.let {
                Glide.with(context)
                    .load(song.img.toUri())
                    .apply(RequestOptions().error(R.drawable.song_500))
                    .into(it)
            }
            artistname?.text = song.artist
            songname?.text = song.title

            if (context.getPreference().getIsPlayingInfo()?.idx == song.idx){
                playing_img?.visibility = View.VISIBLE
                songunderline?.visibility = View.VISIBLE
                songname?.typeface = Typeface.DEFAULT_BOLD
            } else{
                playing_img?.visibility = View.INVISIBLE
                songunderline?.visibility = View.INVISIBLE
                songname?.typeface = Typeface.DEFAULT
            }

            if(mClickListener!=null){
                itemView.setOnClickListener{
                    mClickListener?.onClick(pos)
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.songlist_item,p0,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songlist.size
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        p0.bind(songlist[p1],p1,context)
    }

    fun setAdapterList(list:ArrayList<SongInfo>){
        songlist= list
        notifyDataSetChanged()
    }
}
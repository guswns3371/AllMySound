package com.example.allmysound.Music.SongList

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.allmysound.MainActivity
import com.example.allmysound.R
import com.example.allmysound.SongInfo
import kotlinx.android.synthetic.main.songlist_item.view.*

class SongListAdapter (
    private val context: Context,
    private val songlist: ArrayList<SongInfo>,
    val itemClick : (SongInfo) -> Unit
): androidx.recyclerview.widget.RecyclerView.Adapter<SongListAdapter.MyViewHolder>()  {

    inner class MyViewHolder (itemView : View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        val songimg = itemView?.findViewById<ImageView>(R.id.song_img)
        val artistname = itemView?.findViewById<TextView>(R.id.song_artist)
        val songname = itemView?.findViewById<TextView>(R.id.song_title)
        val playing_img = itemView?.findViewById<ImageView>(R.id.playing_now_img)
        @SuppressLint("ResourceAsColor")
        fun bind(songlist: SongInfo, context: Context) {

            this.songimg?.let {
                Glide.with(context)
                    .load(songlist.img.toUri())
                    .apply(RequestOptions().error(R.drawable.song_500))
                    .into(it)
            }
            artistname?.text = songlist.artist
            songname?.text = songlist.title

            if (MainActivity.prefs.getIsPlayingInfo()?.idx == songlist.idx){
                playing_img?.visibility = View.VISIBLE
                songname?.typeface = Typeface.DEFAULT_BOLD
            } else{
                playing_img?.visibility = View.INVISIBLE
                songname?.typeface = Typeface.DEFAULT
            }


            itemView.setOnClickListener{
                itemClick(songlist)
                it.playing_now_img.visibility = View.VISIBLE
                notifyDataSetChanged()
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
        p0.bind(songlist[p1],context)
    }
}
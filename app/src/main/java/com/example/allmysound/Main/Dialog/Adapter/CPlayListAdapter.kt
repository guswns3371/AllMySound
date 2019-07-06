package com.example.allmysound.Main.Dialog.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.R




class CPlayListAdapter (
    private val context: Context,
    private val songlist: ArrayList<SongInfo>,
    private val numlist: ArrayList<Int>,
    private val orderNum : Int
): androidx.recyclerview.widget.RecyclerView.Adapter<CPlayListAdapter.MyViewHolder>()  {

    interface CustomPlayListClickListener{
        fun onClick(pos:Int)
    }
    var mClickListener: CustomPlayListClickListener? =null

    inner class MyViewHolder (itemView : View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        val songimg = itemView?.findViewById<ImageView>(R.id.song_img)
        val artistname = itemView?.findViewById<TextView>(R.id.song_artist)
        val songname = itemView?.findViewById<TextView>(R.id.song_title)
        val playing_img = itemView?.findViewById<ImageView>(R.id.playing_now_img)
        @SuppressLint("ResourceAsColor")
        fun bind(song: SongInfo, pos : Int, context: Context) {

            this.songimg?.let {
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
//            artistname?.text =text
            artistname?.text = song.artist
            songname?.text = song.title

            if(numlist.indexOf(orderNum) == pos)
                songname?.typeface = Typeface.DEFAULT_BOLD

            if(numlist.indexOf(orderNum) < pos){
                playing_img?.visibility = View.VISIBLE
//                itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            }else{
                playing_img?.visibility = View.GONE
//                itemView.layoutParams = RecyclerView.LayoutParams(0,0)
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
}
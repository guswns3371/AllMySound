package com.example.allmysound.Base.BasicListFrag

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
import com.example.allmysound.Extensions.getPreference
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.R

class BasicListAdapter (
    private val context: Context,
    private val datalist: ArrayList<SongInfo>
): androidx.recyclerview.widget.RecyclerView.Adapter<BasicListAdapter.MyViewHolder>()   {


    interface BasicListClickListener{
        fun onClick(pos:Int)
    }
    var mClickListener: BasicListClickListener? =null

    inner class MyViewHolder  (itemView : View?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        private val songunderline = itemView?.findViewById<LinearLayout>(R.id.song_underline)
        private val songimg = itemView?.findViewById<ImageView>(R.id.song_img)
        private val artistname = itemView?.findViewById<TextView>(R.id.song_artist)
        private val songname = itemView?.findViewById<TextView>(R.id.song_title)
        private val playing_img = itemView?.findViewById<ImageView>(R.id.playing_now_img)

        fun bind(item: SongInfo,pos:Int,context: Context){
            this.songimg?.let {
                Glide.with(context)
                    .load(item.img.toUri())
                    .apply(RequestOptions().error(R.drawable.song_500))
                    .into(it)
            }
            artistname?.text = item.artist
            songname?.text = item.title

            if (context.getPreference().getIsPlayingInfo()?.idx == item.idx){
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.songlist_item,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(datalist[position],position,context)
    }
}
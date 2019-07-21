package com.example.allmysound.Music.PlayList.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.allmysound.Main.Model.PlaylistInfo
import com.example.allmysound.Music.InfoPage.BaseViewHolder
import com.example.allmysound.R

class PlayListFragAdapter(
    private val context: Context,
    private val list: ArrayList<PlaylistInfo>
): androidx.recyclerview.widget.RecyclerView.Adapter<BaseViewHolder<PlaylistInfo>>() {

    companion object {
        private const val TYPE_ONE = 0
        private const val TYPE_TWO = 1
    }

    interface PlayListFragClickListener{
        fun onClick(pos:Int)

        fun onRecentlyAddedClick()
        fun onMostPlayedClick()
        fun onRecentlyPlayedClick()
        fun onLikedSongsClick()
        fun onMakeNewPlaylistClick()
    }
    var mClickListener: PlayListFragClickListener? =null

    inner class MyViewHolderOne (itemView : View?) : BaseViewHolder<PlaylistInfo>(itemView!!)  {
        private val pi_img_a = itemView?.findViewById<ImageView>(R.id.pi_img_a)
        private val pi_img_b = itemView?.findViewById<ImageView>(R.id.pi_img_b)
        private val pi_img_c = itemView?.findViewById<ImageView>(R.id.pi_img_c)
        private val pi_img_d = itemView?.findViewById<ImageView>(R.id.pi_img_d)

        private val pi_recently_added = itemView?.findViewById<CardView>(R.id.pi_recently_added)
        private val pi_most_played = itemView?.findViewById<CardView>(R.id.pi_most_played)
        private val pi_recently_played = itemView?.findViewById<CardView>(R.id.pi_recently_played)
        private val pi_liked_songs = itemView?.findViewById<CardView>(R.id.pi_liked_songs)
        private val pi_make_new = itemView?.findViewById<CardView>(R.id.pi_make_new)

        override fun bind(item:PlaylistInfo,pos: Int,context: Context){

            if(mClickListener!=null){
                pi_recently_added?.setOnClickListener { mClickListener?.onRecentlyAddedClick() }
                pi_most_played?.setOnClickListener { mClickListener?.onMostPlayedClick() }
                pi_recently_played?.setOnClickListener { mClickListener?.onRecentlyPlayedClick() }
                pi_liked_songs?.setOnClickListener { mClickListener?.onLikedSongsClick() }
                pi_make_new?.setOnClickListener { mClickListener?.onMakeNewPlaylistClick() }
            }
        }
    }
    inner class MyViewHolderTwo (itemView : View?) : BaseViewHolder<PlaylistInfo>(itemView!!)  {
        private val playlist_img = itemView?.findViewById<ImageView>(R.id.playlist_img)
        private val playlist_title = itemView?.findViewById<TextView>(R.id.playlist_title)
        private val playlist_cnt_num = itemView?.findViewById<TextView>(R.id.playlist_cnt_num)

        override fun bind(item:PlaylistInfo,pos: Int,context: Context){
            this.playlist_img?.let{
                Glide.with(context)
                    .load(item.img.toUri())
                    .apply(RequestOptions().error(R.drawable.song_500))
                    .into(it)
            }
            playlist_title?.text = item.title
            playlist_cnt_num?.text = item.count
            if(mClickListener!=null){
                itemView.setOnClickListener {
                    mClickListener?.onClick(pos-1)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  BaseViewHolder<PlaylistInfo> {
        return when(viewType){
            TYPE_ONE-> {
                val  view = LayoutInflater.from(context).inflate(R.layout.playlist_item_one,parent,false)
                MyViewHolderOne(view)
            }
            TYPE_TWO -> {
                val  view = LayoutInflater.from(context).inflate(R.layout.playlist_item_two,parent,false)
                MyViewHolderTwo(view)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return list.size+1
    }

    override fun onBindViewHolder(holder:  BaseViewHolder<PlaylistInfo>, position: Int) {
        when(holder){
            is MyViewHolderOne -> holder.bind(list[position],position,context)
            is MyViewHolderTwo -> holder.bind(list[position-1],position,context) // datalist[p1-1] : postion=0에 해당하는 곡이 AlbumInfoHolder로 나타나기때문에
            else -> throw IllegalArgumentException()
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when(position){
            0->TYPE_ONE
            else -> TYPE_TWO
        }
    }
}
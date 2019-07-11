package com.example.allmysound.Music.InfoPage.AlbumInfo.Adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.InfoPage.BaseViewHolder
import com.example.allmysound.R

class AlbumInfoAdapter (
    private val context: Context,
    private val datalist: ArrayList<SongInfo>
): RecyclerView.Adapter<BaseViewHolder<*>>()   {
    companion object {
        private const val TYPE_ALBUMINFO = 0
        private const val TYPE_SONGLIST = 1
    }

    interface AlbumInfoClickListener{
        fun onClick(pos:Int)
    }
    var mClickListener: AlbumInfoClickListener? =null

    inner class SongListHolder (itemView : View?) : BaseViewHolder<SongInfo>(itemView!!) {
        val infoNum = itemView?.findViewById<TextView>(R.id.info_num)
        val infoTitle = itemView?.findViewById<TextView>(R.id.info_title)
        val infoPlayingImg = itemView?.findViewById<ImageView>(R.id.info_isplaying_img)
        override fun bind(item: SongInfo, pos : Int, context: Context) {

            infoNum?.text = item.album_track_num
            infoTitle?.text = item.title

            if (MainActivity.prefs.getIsPlayingInfo()?.idx == item.idx){
                infoPlayingImg?.visibility = View.VISIBLE
                infoTitle?.typeface = Typeface.DEFAULT_BOLD
            } else{
                infoPlayingImg?.visibility = View.INVISIBLE
                infoTitle?.typeface = Typeface.DEFAULT
            }
            if(mClickListener!=null){
                itemView.setOnClickListener{
                    mClickListener?.onClick(pos)
                    notifyDataSetChanged()
                }
            }
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<*>  {
        return when(p1){
            TYPE_ALBUMINFO -> {
                val  view = LayoutInflater.from(context).inflate(R.layout.infolist_songitem,p0,false)
                SongListHolder(view)
            }
            TYPE_SONGLIST -> {
                val  view = LayoutInflater.from(context).inflate(R.layout.infolist_songitem,p0,false)
                SongListHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(p0: BaseViewHolder<*> , p1: Int) {
        val element = datalist[p1]
        when(p0){
            is SongListHolder -> p0.bind(element,p1,context)
        }
    }

}
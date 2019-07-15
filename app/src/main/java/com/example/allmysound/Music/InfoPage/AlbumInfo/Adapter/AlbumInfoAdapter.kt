package com.example.allmysound.Music.InfoPage.AlbumInfo.Adapter

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
    var diskZero = true
    var diskOne = true
    var diskTwo = true

    inner class SongListHolder (itemView : View?) : BaseViewHolder<SongInfo>(itemView!!) {
        private val infoDisk = itemView?.findViewById<TextView>(R.id.info_disk)
        private val infoUnderline = itemView?.findViewById<LinearLayout>(R.id.info_underline)
        private val infoNum = itemView?.findViewById<TextView>(R.id.info_num)
        private val infoTitle = itemView?.findViewById<TextView>(R.id.info_title)
        private val infoPlayingImg = itemView?.findViewById<ImageView>(R.id.info_isplaying_img)
        override fun bind(item: SongInfo, pos: Int, context: Context) {

            when (val trackNum = item.album_track_num.toInt()) {
                in 2000..2999 -> {
                    infoNum?.text = "${trackNum %2000}"
                    infoDisk?.text = "디스크 2"
                    if(diskTwo)
                        infoDisk?.visibility = View.VISIBLE
                    else
                        infoDisk?.visibility = View.GONE
                    diskTwo = false
                }
                in 1000..1999 -> {
                    infoNum?.text = "${trackNum %1000}"
                    infoDisk?.text = "디스크 1"
                    if(diskOne)
                        infoDisk?.visibility = View.VISIBLE
                    else
                        infoDisk?.visibility = View.GONE
                    diskOne = false
                }
                in 0..999 -> {
                    infoNum?.text = "$trackNum"
                }
            }
            infoTitle?.text = item.title
            if (MainActivity.prefs.getIsPlayingInfo()?.idx == item.idx){
                infoPlayingImg?.visibility = View.VISIBLE
                infoUnderline?.visibility = View.VISIBLE
                infoTitle?.typeface = Typeface.DEFAULT_BOLD
            } else{
                infoPlayingImg?.visibility = View.INVISIBLE
                infoUnderline?.visibility = View.INVISIBLE
                infoTitle?.typeface = Typeface.DEFAULT
            }

            if(mClickListener!=null){
                itemView.setOnClickListener{
                    mClickListener?.onClick(pos-1) // position =0 에 앨범정보가 들어가있기 때문에 pos-1 해준다
                    myNotifyDataSetChanged()
                }
            }
        }
    }

    inner class AlbumInfoHolder(itemView: View?):BaseViewHolder<SongInfo>(itemView!!){
        private val image = itemView?.findViewById<ImageView>(R.id.ai_image)
        private val album = itemView?.findViewById<TextView>(R.id.ai_album)
        private val aritst = itemView?.findViewById<TextView>(R.id.ai_aritst)
        private val genre = itemView?.findViewById<TextView>(R.id.ai_genre)
        private val date = itemView?.findViewById<TextView>(R.id.ai_date)
        override fun bind(item: SongInfo, pos: Int, context: Context) {
            this.image?.let {
                Glide.with(context)
                    .load(item.img.toUri())
                    .apply(RequestOptions().error(R.drawable.song_500))
                    .into(it)
            }
            album?.text = item.album
            aritst?.text = item.artist
            genre?.text = item.genre
            date?.text = item.date
        }
    }

    fun myNotifyDataSetChanged(){
        diskZero = true
        diskOne = true
        diskTwo = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder<*>  {
        return when(p1){
            TYPE_ALBUMINFO -> {
                val  view = LayoutInflater.from(context).inflate(R.layout.infolist_albuminfo,p0,false)
                AlbumInfoHolder(view)
            }
            TYPE_SONGLIST -> {
                val  view = LayoutInflater.from(context).inflate(R.layout.infolist_songitem,p0,false)
                SongListHolder(view)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount(): Int {
        return datalist.size+1
    }

    override fun onBindViewHolder(p0: BaseViewHolder<*> , p1: Int) {
        when(p0){
            is AlbumInfoHolder -> p0.bind(datalist[p1],p1,context)
            is SongListHolder -> p0.bind(datalist[p1-1],p1,context) // datalist[p1-1] : postion=0에 해당하는 곡이 AlbumInfoHolder로 나타나기때문에
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0->TYPE_ALBUMINFO
            else -> TYPE_SONGLIST
        }
    }

}
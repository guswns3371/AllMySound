package com.example.allmysound.Music.InfoPage.ArtistInfo.AritstInfo

import android.content.Context
import android.graphics.Typeface
import android.util.Log
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

class ArtistInfoAdapter(
    private val context: Context,
    private val datalist: ArrayList<SongInfo>
): RecyclerView.Adapter<BaseViewHolder<*>>()   {

    interface ArtistInfoClickListener{
        fun onClick(pos:Int)
    }
    var mClickListener: ArtistInfoClickListener? =null
    lateinit var albumlist : ArrayList<String>
    var booleanlist : Array<Boolean> = emptyArray()
    var diskZero = true
    var diskOne = true
    var diskTwo = true

    fun initAlbumlist(albumlist : ArrayList<String>){
        this.albumlist= albumlist
        booleanlist =Array(albumlist.size) {true}
    }

    inner class AlbumListHolder (itemView : View?) : BaseViewHolder<SongInfo>(itemView!!) {
        private val artisinfoRV = itemView?.findViewById<RecyclerView>(R.id.artisinfo_RV)

        private val albuminfoLO = itemView?.findViewById<LinearLayout>(R.id.albuminfo_LO)
        private val songitemLO = itemView?.findViewById<LinearLayout>(R.id.songitem_LO)

        private val image = itemView?.findViewById<ImageView>(R.id.ai_image)
        private val album = itemView?.findViewById<TextView>(R.id.ai_album)
        private val aritst = itemView?.findViewById<TextView>(R.id.ai_aritst)
        private val genre = itemView?.findViewById<TextView>(R.id.ai_genre)
        private val date = itemView?.findViewById<TextView>(R.id.ai_date)

        private val infoDisk = itemView?.findViewById<TextView>(R.id.info_disk)
        private val infoUnderline = itemView?.findViewById<LinearLayout>(R.id.info_underline)
        private val infoNum = itemView?.findViewById<TextView>(R.id.info_num)
        private val infoTitle = itemView?.findViewById<TextView>(R.id.info_title)
        private val infoPlayingImg = itemView?.findViewById<ImageView>(R.id.info_isplaying_img)

        override fun bind(item: SongInfo, pos: Int, context: Context) {

            val albumName = item.album
            val index = albumlist.indexOf(albumName)
            if(booleanlist[index]){
                albuminfoLO?.visibility = View.VISIBLE
                booleanlist[index] = false
            } else{
                albuminfoLO?.visibility = View.GONE
            }


            /**albuminfoLO*/
            this.image?.let {
                Glide.with(context)
                    .load(item.img.toUri())
                    .apply(RequestOptions().error(R.drawable.song_500))
                    .into(it)
            }
            album?.text = item.album
            aritst?.text = item.title
            genre?.text = item.genre
            date?.text = item.date

            /**songitemLO*/
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
                songitemLO?.setOnClickListener{
                    mClickListener?.onClick(pos)
                    myNotifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val  view = LayoutInflater.from(context).inflate(R.layout.infolist_artistinfo_item,parent,false)
        return AlbumListHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        holder.bind(datalist[position],position,context)
    }

    fun myNotifyDataSetChanged(){
        booleanlist =Array(albumlist.size) {true}
        diskZero = true
        diskOne = true
        diskTwo = true
        notifyDataSetChanged()
    }
}
package com.example.allmysound.Music.InfoPage.ArtistInfo.AritstInfo

import android.content.Context
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
    inner class AlbumListHolder (itemView : View?) : BaseViewHolder<SongInfo>(itemView!!) {
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
            aritst?.text = item.title
            genre?.text = item.title
            date?.text = item.date

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val  view = LayoutInflater.from(context).inflate(R.layout.infolist_albuminfo,parent,false)
        return AlbumListHolder(view)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        holder.bind(datalist[position],position,context)
    }
}
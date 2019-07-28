package com.example.allmysound.Music.InfoPage.ArtistInfo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.InfoPage.AlbumInfo.Adapter.AlbumInfoAdapter
import com.example.allmysound.R
import java.util.stream.Collectors

class ArtistInfoAdapter(
    private val context: Context,
    private val datalist: ArrayList<SongInfo>
): RecyclerView.Adapter<ArtistInfoAdapter.AlbumListHolder>()   {

    interface ArtistInfoClickListener{
        fun onClick(pos:Int)
    }
    var mClickListener: ArtistInfoClickListener? =null
    lateinit var albumlist : ArrayList<String>
    fun initAlbumlist(albumlist : ArrayList<String>){
        this.albumlist= albumlist
    }

    inner class AlbumListHolder (itemView : View?) : RecyclerView.ViewHolder(itemView!!) {
        private val mRecyclerView = itemView?.findViewById<RecyclerView>(R.id.artisinfo_RV)
        fun bindRecyclerView(item : ArrayList<SongInfo>, pos: Int, context: Context){
            val albumName = albumlist[pos] // 앨범 이름
            var posIndatalist=0
            for(i in 0 until datalist.size){
                if(datalist[i].album==albumName){
                    posIndatalist=i // datalist속 특정 앨범의 첫 번째 곡의 포지션값
                    break
                }
            }
            val mAdapter = AlbumInfoAdapter(context,item)
            mAdapter.mClickListener = object  : AlbumInfoAdapter.AlbumInfoClickListener{
                override fun onClick(pos: Int) {
                    MainActivity.createMainPresenter().LinkDataList(datalist)
                    MainActivity.createMainPresenter().AlbumlinkDataIndex(pos+posIndatalist)
                    MainActivity.createMainPresenter().checkIsPlaying()
                    MainActivity.createMainPresenter().setRandomIdxNumList()
                    myNotifyDataSetChanged()
                }
            }
            MainActivity.createMainPresenter().LinkAdapter(mAdapter)

            mRecyclerView?.adapter = mAdapter
            mRecyclerView?.layoutManager= LinearLayoutManager(context)
            mRecyclerView?.setHasFixedSize(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumListHolder {
        val  view = LayoutInflater.from(context).inflate(R.layout.infolist_artistinfo_item,parent,false)
        return AlbumListHolder(view)
    }

    override fun getItemCount(): Int {
        return albumlist.size
    }

    override fun onBindViewHolder(holder:AlbumListHolder, position: Int) {
        val list = datalist.stream().filter { it.album==albumlist[position] }.collect(Collectors.toList())
        holder.bindRecyclerView(ArrayList(list),position,context)
    }

    fun myNotifyDataSetChanged(){
        notifyDataSetChanged()
    }
}
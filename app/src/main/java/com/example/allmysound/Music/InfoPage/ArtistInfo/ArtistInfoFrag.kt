package com.example.allmysound.Music.InfoPage.ArtistInfo

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.InfoPage.ArtistInfo.Adapter.ArtistInfoAdapter
import com.example.allmysound.R


class ArtistInfoFrag: Fragment() , ArtistContract.View{
    override fun connectFragment(frag: Fragment) {
    }

    override fun moveToFragment(frag: Fragment, key: String?, data: Any) {
    }

    val DATA_RECEIVE = "ArtistInfo"
    private lateinit var presenter : ArtistPresenter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var mRecyclerView : androidx.recyclerview.widget.RecyclerView
    private lateinit var datalist : ArrayList<SongInfo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_artistinfo,container,false)

        mRecyclerView = view.findViewById(R.id.artistinfo_RV)
        linearLayoutManager = LinearLayoutManager(activity!!)
        initPresenter()
        initRecyclerView()

        return view
    }
    private fun initPresenter(){
        presenter = ArtistPresenter(activity!!)
        presenter.setView(this)

        val songinfo = getFragArgs()
        val query =
            MediaStore.Audio.Media.ARTIST+"== '${songinfo.artist.replace("'","''")}'"
        val orderBy =
                    MediaStore.Audio.AudioColumns.ALBUM + " ASC ,"+
                    MediaStore.Audio.AudioColumns.TRACK + " ASC"

        datalist=  presenter.loadDataByQuery(activity!!,
            query,
            orderBy)
    }
    private fun getAlbumList() : ArrayList<String>{
        val datalistTwo = ArrayList<String>()
        for(i in 0 until datalist.size){
            if(!datalistTwo.contains(datalist[i].album))
                datalistTwo.add(datalist[i].album)
        }
        return datalistTwo
    }
    private fun  initRecyclerView(){
        val mAdapter = ArtistInfoAdapter(activity!!,datalist)
        mAdapter.initAlbumlist(getAlbumList())
        mAdapter.mClickListener = object  : ArtistInfoAdapter.ArtistInfoClickListener {
            override fun onClick(pos: Int) {
                MainActivity.createMainPresenter().LinkDataList(datalist)
                MainActivity.createMainPresenter().ArtistIlinkDataIndex(pos)
                MainActivity.createMainPresenter().checkIsPlaying()
                MainActivity.createMainPresenter().setRandomIdxNumList()
            }
        }
        MainActivity.createMainPresenter().LinkAdapter(mAdapter)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager=linearLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }
    override fun getFragArgs(): SongInfo = arguments!!.getSerializable(DATA_RECEIVE) as SongInfo
}
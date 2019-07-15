package com.example.allmysound.Music.InfoPage.AlbumInfo

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.InfoPage.AlbumInfo.Adapter.AlbumInfoAdapter
import com.example.allmysound.R


class AlbumInfoFrag: Fragment() , AlbumContract.View {

    val DATA_RECEIVE = "AlbumInfo"
    private lateinit var presenter : AlbumPresenter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var mRecyclerView : androidx.recyclerview.widget.RecyclerView
    private lateinit var datalist : ArrayList<SongInfo>

    override fun onPause() {
        Log.e("FragLifeCycle","AlbumInfoFrag_onPause")
        super.onPause()
    }
    override fun onDestroyView() {
        Log.e("FragLifeCycle","AlbumInfoFrag_onDestroyView")
        super.onDestroyView()
    }
    override fun onDestroy() {
        Log.e("FragLifeCycle","AlbumInfoFrag_onDestroy")
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_albuminfo,container,false)
        Log.e("FragLifeCycle","AlbumInfoFrag_onCreateView")

        mRecyclerView = view.findViewById(R.id.albuminfo_RV)
        linearLayoutManager = LinearLayoutManager(activity!!)
        initPresenter()
        initRecyclerView()
        return view
    }
    private fun initPresenter(){
        presenter = AlbumPresenter(activity!!)
        presenter.setView(this)

        val songinfo = getFragArgs()
        val queryTwo = if(songinfo.albumArtist == "Unknown")  MediaStore.Audio.Media.ARTIST+"== '${songinfo.artist.replace("'","''")}'"
        else " album_artist == '${songinfo.albumArtist.replace("'","''")}' "

        val query = MediaStore.Audio.Media.ALBUM +" == '${songinfo.album.replace("'","''")}' "+
                " and "+queryTwo
        val orderBy = MediaStore.Audio.AudioColumns.TRACK+" ASC"

        datalist=  presenter.loadDataByQuery(activity!!,
            query,
            orderBy )
    }

    private fun initRecyclerView(){
        val mAdapter = AlbumInfoAdapter(activity!!,datalist)
        mAdapter.mClickListener = object  : AlbumInfoAdapter.AlbumInfoClickListener{
            override fun onClick(pos: Int) {
                MainActivity.createMainPresenter().LinkDataList(datalist)
                MainActivity.createMainPresenter().AlbumlinkDataIndex(pos)
                MainActivity.createMainPresenter().checkIsPlaying()
                MainActivity.createMainPresenter().setRandomIdxNumList()
            }
        }
        MainActivity.createMainPresenter().LinkAdapter(mAdapter)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager=linearLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }
    override fun getFragArgs(): SongInfo =arguments!!.getSerializable(DATA_RECEIVE) as SongInfo
    override fun showToast(message: String) {
        Toast.makeText(activity!!, message,Toast.LENGTH_SHORT).show()
    }


}
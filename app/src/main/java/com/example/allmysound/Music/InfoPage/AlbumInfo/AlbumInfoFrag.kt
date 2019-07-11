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
        datalist=  presenter.loadDataByQuery(activity!!,
            MediaStore.Audio.Media.ALBUM +" == '${getFragIntent()}' ",
            MediaStore.Audio.AudioColumns.TRACK)
    }

    private fun initRecyclerView(){
        val mAdapter = AlbumInfoAdapter(activity!!,datalist)
        mAdapter.mClickListener = object  : AlbumInfoAdapter.AlbumInfoClickListener{
            override fun onClick(pos: Int) {
                MainActivity.createMainPresenter().AlbumlinkData(datalist)
                MainActivity.createMainPresenter().AlbumlinkDataIndex(pos)
                MainActivity.createMainPresenter().checkIsPlaying()
                MainActivity.createMainPresenter().setRandomIdx_NumList()
            }
        }
        MainActivity.createMainPresenter().AlbumlinkAdapter(mAdapter)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager=linearLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }
    override fun getFragIntent(): String? =arguments!!.getString(DATA_RECEIVE)
    override fun showToast(message: String) {
        Toast.makeText(activity!!, message,Toast.LENGTH_SHORT).show()
    }


}
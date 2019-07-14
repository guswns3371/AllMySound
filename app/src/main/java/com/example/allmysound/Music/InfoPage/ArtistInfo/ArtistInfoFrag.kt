package com.example.allmysound.Music.InfoPage.ArtistInfo

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.InfoPage.ArtistInfo.AritstInfo.ArtistInfoAdapter
import com.example.allmysound.R


class ArtistInfoFrag: Fragment() , ArtistContract.View{

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
        val query = MediaStore.Audio.Media.ARTIST +" == '${songinfo.artist.replace("'","''")}' "
        val orderBy =
                    MediaStore.Audio.AudioColumns.ALBUM + " ASC ,"+
                    MediaStore.Audio.AudioColumns.TRACK + " ASC"

        datalist=  presenter.loadDataByQuery(activity!!,
            query,
            orderBy)
    }
    private fun  initRecyclerView(){
        val mAdapter = ArtistInfoAdapter(activity!!,datalist)
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
    override fun showToast(message: String) {
        Toast.makeText(activity!!, message,Toast.LENGTH_SHORT).show()
    }
}
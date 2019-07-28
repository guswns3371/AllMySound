package com.example.allmysound.Music.PlayList

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmysound.Base.BasicListFrag.BasicListFrag
import com.example.allmysound.Main.Model.PlaylistInfo
import com.example.allmysound.Music.PlayList.Adapter.PlayListFragAdapter
import com.example.allmysound.R
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import kotlinx.android.synthetic.main.playlist_item_one.*
import com.example.allmysound.Base.Extensions.showToast

class PlayListFrag: Fragment(),PlayListContract.View {

    private lateinit var mRecyclerView : androidx.recyclerview.widget.RecyclerView
    private lateinit var mFastScrollView : FastScrollerView
    private lateinit var mFastScrollThumbView : FastScrollerThumbView
    private lateinit var presenter : PlayListPresenter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var playlist : ArrayList<PlaylistInfo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_playlist,container,false)

        mRecyclerView = view.findViewById(R.id.playlistfrag_RV)
//        mFastScrollView = view.findViewById(R.id.fastscroller)
//        mFastScrollThumbView = view.findViewById(R.id.fastscroller_thumb)
        linearLayoutManager = LinearLayoutManager(activity!!)
        initPresenter()
        initRecyclerView()
        return view
    }

    private fun initPresenter(){
        presenter = PlayListPresenter(activity!!)
        presenter.setView(this)
        val arrayList = ArrayList<PlaylistInfo>()
        for(i in 0..10){
            arrayList.add(PlaylistInfo(i.toString(),"","title $i","${i+1}"))
        }
        playlist = arrayList
    }
    private fun initRecyclerView(){
        val myAdapter = PlayListFragAdapter(activity!!, playlist)
        myAdapter.mClickListener = object : PlayListFragAdapter.PlayListFragClickListener{
            override fun onClick(pos: Int) {
                activity!!.showToast("$pos")
            }

            override fun onRecentlyAddedClick() {
                val query = MediaStore.Audio.Media.IS_MUSIC +" != 0"
                val orderBy = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"
                val basicFrag = BasicListFrag(presenter.loadDataByQuery(activity!!,
                    query,
                    orderBy))
                val toolbarTitle ="${pi_ra_txt.text}"
                moveToFragment(basicFrag,basicFrag.DATA_RECEIVE,toolbarTitle)
            }

            override fun onMostPlayedClick() {
                activity!!.showToast("onMostPlayedClick")
            }

            override fun onRecentlyPlayedClick() {
                activity!!.showToast("onRecentlyPlayedClick")
            }

            override fun onLikedSongsClick() {
                activity!!.showToast("onLikedSongsClick")
            }

            override fun onMakeNewPlaylistClick() {
                activity!!.showToast("onMakeNewPlaylistClick")
            }
        }
        mRecyclerView.adapter = myAdapter
        mRecyclerView.layoutManager=linearLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }

    override fun connectFragment(frag: Fragment) {
        fragmentManager!!.beginTransaction().replace(R.id.frag_container, frag).commit()
    }

    override fun moveToFragment(frag: Fragment, key: String?, data: Any) {
        key?.let{
            val args = Bundle()
            args.putString(it,data as String)
            frag.arguments = args
        }
        fragmentManager!!.beginTransaction().replace(R.id.frag_container, frag).addToBackStack(null).commit()
    }
}
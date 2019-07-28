package com.example.allmysound.Music.SongList

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.R
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.SongList.Adapter.SongListAdapter
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView

class SongListFrag: Fragment(),SongListContract.View {
    override fun connectFragment(frag: Fragment) {
    }

    override fun moveToFragment(frag: Fragment, key: String?, data: Any) {
    }

    private lateinit var mRecyclerView : androidx.recyclerview.widget.RecyclerView
    private lateinit var mFastScrollView : FastScrollerView
    private lateinit var mFastScrollThumbView : FastScrollerThumbView
    private lateinit var presenter : SongListPresenter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var songlist : ArrayList<SongInfo>

    override fun onPause() {
        Log.e("FragLifeCycle","SongListFrag_onPause")
        super.onPause()
    }
    override fun onDestroyView() {
        Log.e("FragLifeCycle","SongListFrag_onDestroyView")
        super.onDestroyView()
    }
    override fun onDestroy() {
        Log.e("FragLifeCycle","SongListFrag_onDestroy")
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songlist,container,false)
        Log.e("FragLifeCycle","SongListFrag_onCreateView")

        mRecyclerView = view.findViewById(R.id.songlist_RV)
        mFastScrollView = view.findViewById(R.id.fastscroller)
        mFastScrollThumbView = view.findViewById(R.id.fastscroller_thumb)
        linearLayoutManager = LinearLayoutManager(activity!!)
        initPresenter()
        initRecyclerView()
        initFastCrollView()

        return view
    }

    fun initPresenter(){
        presenter = SongListPresenter(activity!!)
        presenter.setView(this)
        val query = MediaStore.Audio.Media.IS_MUSIC +" != 0"
        val orderBy = "upper(${MediaStore.Audio.AudioColumns.TITLE})"
//        val orderBy = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"
        songlist = presenter.loadDataByQuery(activity!!,
            query,
            orderBy)
    }
    fun initRecyclerView(){
        val myAdapter = SongListAdapter(activity!!, songlist)
        myAdapter.mClickListener = object : SongListAdapter.SongListClickListener{
            override fun onClick(pos: Int) {
                MainActivity.createMainPresenter().LinkDataList(songlist)
                MainActivity.createMainPresenter().SonglinkDataIndex(pos)
                MainActivity.createMainPresenter().checkIsPlaying()
                MainActivity.createMainPresenter().setRandomIdxNumList()
            }
        }
        /**
        if(MainActivity.createMainPresenter().isNewState)  // 앱이 처음 실행되었을때 만 songlist를 연결
            MainActivity.createMainPresenter().LinkDataList(songlist) */

        MainActivity.createMainPresenter().LinkAdapter(myAdapter)
        mRecyclerView.adapter = myAdapter
        mRecyclerView.layoutManager=linearLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }
    fun initFastCrollView(){
        mFastScrollView.setupWithRecyclerView(
            mRecyclerView,
            { position ->
                val item = songlist[position] // Get your model object
                // or fetch the section at [position] from your database
                val text = if(position%100==0 || position==0 || position == (songlist.size-1)/2 || position == songlist.size-1)
                    item.title.substring(0, 1).toUpperCase() else "."
                FastScrollItemIndicator.Text(
                    text // Grab the first letter and capitalize it
                ) // Return a text indicator
            }
        )
        mFastScrollThumbView.setupWithFastScroller(mFastScrollView)
        mFastScrollView.itemIndicatorSelectedCallbacks += object : FastScrollerView.ItemIndicatorSelectedCallback {
            override fun onItemIndicatorSelected(
                indicator: FastScrollItemIndicator,
                indicatorCenterY: Int,
                itemPosition: Int
            ) {
                mRecyclerView.stopScroll()
//                smoothScroller.targetPosition = itemPosition
//                linearLayoutManager.startSmoothScroll(smoothScroller)
                linearLayoutManager.scrollToPosition(itemPosition)
            }
        }
    }


    }

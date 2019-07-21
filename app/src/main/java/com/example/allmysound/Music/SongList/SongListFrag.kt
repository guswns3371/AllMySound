package com.example.allmysound.Music.SongList

import android.Manifest
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
import com.example.allmysound.R
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.SongList.Adapter.SongListAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
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
    private lateinit var songInfos : ArrayList<SongInfo>

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
        Permission()
        initPresenter()
        initRecyclerView()
        initFastCrollView()

        return view
    }

    fun Permission(){
        val permissionlistener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.e("Songlist_Permission", "Permission Granted")
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Log.e("Songlist_Permission", "Permission Denied$deniedPermissions")
                activity!!.finish()
            }
        }
        TedPermission.with(activity!!)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("퍼미션 거부시 ,서비스를 이용 할 수 없습니다\n\n설정에서 퍼미션을 승인하세요 ")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }
    fun initPresenter(){
        presenter = SongListPresenter(activity!!)
        presenter.setView(this)
        val query = MediaStore.Audio.Media.IS_MUSIC +" != 0"
        val orderBy = "upper(${MediaStore.Audio.AudioColumns.TITLE})"
//        val orderBy = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"
        songInfos = presenter.loadDataByQuery(activity!!,
            query,
            orderBy)
    }
    fun initRecyclerView(){
        val myAdapter = SongListAdapter(activity!!, songInfos)
        myAdapter.mClickListener = object : SongListAdapter.SongListClickListener{
            override fun onClick(pos: Int) {
                MainActivity.createMainPresenter().LinkDataList(songInfos)
                MainActivity.createMainPresenter().SonglinkDataIndex(pos)
                MainActivity.createMainPresenter().checkIsPlaying()
                MainActivity.createMainPresenter().setRandomIdxNumList()
            }
        }
        if(MainActivity.createMainPresenter().isNewState)  // 앱이 처음 실행되었을때 만 songlist를 연결
            MainActivity.createMainPresenter().LinkDataList(songInfos)

        MainActivity.createMainPresenter().LinkAdapter(myAdapter)
        mRecyclerView.adapter = myAdapter
        mRecyclerView.layoutManager=linearLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }
    fun initFastCrollView(){
        mFastScrollView.setupWithRecyclerView(
            mRecyclerView,
            { position ->
                val item = songInfos[position] // Get your model object
                // or fetch the section at [position] from your database
                val text = if(position%100==0 || position==0 || position == (songInfos.size-1)/2 || position == songInfos.size-1)
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

package com.example.allmysound.Music.SongList

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.example.allmysound.MainActivity
import com.example.allmysound.R
import com.example.allmysound.SongInfo
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView

class SongListFrag: Fragment(),SongListContract.View {

    private lateinit var mRecyclerView : androidx.recyclerview.widget.RecyclerView
    private lateinit var mFastScrollView : FastScrollerView
    private lateinit var mFastScrollThumbView : FastScrollerThumbView
    private lateinit var presenter : SongListPresenter
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var songInfos : ArrayList<SongInfo>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songlist,container,false)

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

    override fun showToast(message: String) {
        Toast.makeText(activity!!,message,Toast.LENGTH_SHORT).show()
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
        songInfos = presenter.loadSong()
    }
    fun initRecyclerView(){
        val myAdapter = SongListAdapter(activity!!,songInfos){
            MainActivity.createMainPresenter().linkData(it)
            MainActivity.createMainPresenter().checkIsPlaying()
        }
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
                FastScrollItemIndicator.Text(
                    item.title.substring(0, 1).toUpperCase() // Grab the first letter and capitalize it
                ) // Return a text indicator
            }
        )
        mFastScrollThumbView.setupWithFastScroller(mFastScrollView)
        val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(activity!!) {
            override fun getVerticalSnapPreference(): Int = SNAP_TO_START
        }
        mFastScrollView.itemIndicatorSelectedCallbacks += object : FastScrollerView.ItemIndicatorSelectedCallback {
            override fun onItemIndicatorSelected(
                indicator: FastScrollItemIndicator,
                indicatorCenterY: Int,
                itemPosition: Int
            ) {
                mRecyclerView.stopScroll()
                smoothScroller.targetPosition = itemPosition
                linearLayoutManager.startSmoothScroll(smoothScroller)
            }
        }
    }
    }

package com.example.allmysound.Music.SongList

import android.Manifest
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.allmysound.MainActivity
import com.example.allmysound.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class SongListFrag: Fragment(),SongListContract.View {


    private lateinit var mRecyclerView : androidx.recyclerview.widget.RecyclerView
    private lateinit var presenter : SongListPresenter
    var mp :  MediaPlayer? = MediaPlayer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_songlist,container,false)

        Permission()
        mRecyclerView = view.findViewById(R.id.songlist_RV)
        presenter = SongListPresenter(activity!!)
        presenter.setView(this)


        val songInfos = presenter.loadSong()

        val myAdapter = SongListAdapter(activity!!,songInfos){
            MainActivity.createMainPresenter().linkData(it)
            MainActivity.createMainPresenter().checkIsPlaying()
        }
        mRecyclerView.adapter = myAdapter

        val lm = androidx.recyclerview.widget.LinearLayoutManager(activity)
        mRecyclerView.layoutManager=lm
        mRecyclerView.setHasFixedSize(true)

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
}
package com.example.allmysound.Music.InfoPage.ArtistInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.allmysound.Music.InfoPage.AlbumInfo.AlbumContract
import com.example.allmysound.Music.InfoPage.AlbumInfo.AlbumPresenter
import com.example.allmysound.R


class ArtistInfoFrag: Fragment() , ArtistContract.View{

    val DATA_RECEIVE = "ArtistInfo"
    private lateinit var presenter : ArtistPresenter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_artistinfo,container,false)
        initPresenter()
        getData()
        return view
    }
    private fun initPresenter(){
        presenter = ArtistPresenter(activity!!)
        presenter.setView(this)
    }
    private fun getData(){
        val args = arguments!!.getString(DATA_RECEIVE)
        showToast(args)
    }
    override fun showToast(message: String) {
        Toast.makeText(activity!!, message,Toast.LENGTH_SHORT).show()
    }
}
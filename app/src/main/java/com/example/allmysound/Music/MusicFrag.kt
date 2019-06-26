package com.example.allmysound.Music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.allmysound.R
import kotlinx.android.synthetic.main.fragment_music.*
import kotlinx.android.synthetic.main.fragment_music.view.*

class MusicFrag: Fragment(),MusicFragContract.View {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_music,container,false)

        view.musicText.setOnClickListener { showToast("music") }

        return view
    }
    override fun showToast(message: String) {
        Toast.makeText(activity,message,Toast.LENGTH_SHORT).show()
    }
}
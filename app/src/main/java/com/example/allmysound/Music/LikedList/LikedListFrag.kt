package com.example.allmysound.Music.LikedList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.allmysound.R
import kotlinx.android.synthetic.main.fragment_likedlist.view.*

class LikedListFrag: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_likedlist,container,false)
        view.textView2.setOnClickListener {   Toast.makeText(activity,"textView2", Toast.LENGTH_SHORT).show() }

        return view
    }
}
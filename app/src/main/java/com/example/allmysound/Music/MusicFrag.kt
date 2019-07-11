package com.example.allmysound.Music

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.allmysound.R
import com.google.android.material.tabs.TabLayout

class MusicFrag: Fragment(){

    private lateinit var tabs: TabLayout
    private lateinit var viewpager: ViewPager

    override fun onPause() {
        Log.e("FragLifeCycle","MusicFrag_onPause")
        super.onPause()
    }
    override fun onDestroyView() {
        Log.e("FragLifeCycle","MusicFrag_onDestroyView")
        super.onDestroyView()
    }
    override fun onDestroy() {
        Log.e("FragLifeCycle","MusicFrag_onDestroy")
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_music,container,false)
        Log.e("FragLifeCycle","MusicFrag_onCreateView")
         tabs = view.findViewById(R.id.tabs)
         viewpager = view.findViewById(R.id.viewPager)

         viewpagerInit()

        return view
    }

    fun viewpagerInit(){
        viewpager.adapter = MusicFragPagerAdapter(fragmentManager)
        viewpager.addOnPageChangeListener(object  : TabLayout.TabLayoutOnPageChangeListener(tabs){})
        tabs.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(viewpager){})
        tabs.setupWithViewPager(viewpager)
        tabs.setSelectedTabIndicatorColor(Color.parseColor("#000000"))
        tabs.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"))
        tabs.tabGravity = TabLayout.GRAVITY_FILL
        viewpager.currentItem = 0
    }

}
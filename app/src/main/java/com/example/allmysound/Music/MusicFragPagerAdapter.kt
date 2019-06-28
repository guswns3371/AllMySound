package com.example.allmysound.Music

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.allmysound.Music.LikedList.LikedListFrag
import com.example.allmysound.Music.PlayList.PlayListFrag
import com.example.allmysound.Music.SongList.SongListFrag

class MusicFragPagerAdapter(fragmentManager: FragmentManager?) :
    FragmentStatePagerAdapter(fragmentManager) {
    private var tabTitles =  arrayOf("좋아요", "플레이리스트","노래")
    override fun getItem(p0: Int): androidx.fragment.app.Fragment? {
        return when (p0){
            0->  SongListFrag()
            1-> PlayListFrag()
            2-> LikedListFrag()
            else -> null
        }
    }

    override fun getCount(): Int {
        return 3
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}
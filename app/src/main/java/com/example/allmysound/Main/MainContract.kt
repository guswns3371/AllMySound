package com.example.allmysound.Main

import android.content.Context
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.allmysound.Base.BaseContract
import com.example.allmysound.Main.Dialog.Adapter.CPlayListAdapter
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.InfoPage.AlbumInfo.Adapter.AlbumInfoAdapter
import com.example.allmysound.Music.SongList.SongListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout

interface MainContract {
    interface View:BaseContract.View{
        override fun showToast(message: String)

        fun setToolbar(toolbar: Toolbar)
        fun moveToFragment(frag: Fragment,key : String?,data:String?)
        fun connectMusicFragment()
        fun setControllerAlpha(distance:Float)
        fun setImageSize(distance:Float)

        fun showMoreBtn()
        fun setSongTitle(text:String)
        fun setSongInnerTitle(text:String)
        fun setSongAlbum(text:String)
        fun setSongArtist(text:String)
        fun setSongTime(text:String)
        fun setSongAlbumArt(uri: String)

        fun setPastTimeText(text: String)

        fun changePlayBtn(isPlay:Boolean)
        fun changeRotateBtn(isRotate:Boolean)
        fun changeShuffleBtn(isShuffle:Boolean)
        fun changeLikeBtn(isLike:Boolean)

        fun setMusicSeekBarListener(listener : SeekBar.OnSeekBarChangeListener)
        fun setMusicSeekBarMax(max : Int)
        fun setMusicSeekBarProgress(progress: Int)
        fun updateMusicSeekBarNTime()

    }
    interface Presenter:BaseContract.Presenter<View>{
        override fun setView(view: View)
        override fun releaseView()

        /**SongListAdapter*/
        fun SonglinkData(songInfolist: ArrayList<SongInfo>)
        fun SonglinkAdapter(songListAdapter: SongListAdapter)
        fun SonglinkDataIndex(idx : Int)
        fun SonglinkDataUpdateIndex(idx : Int)

        /**CPlayListAdapter*/
        fun PlaylistllinkData(songInfolist:  ArrayList<SongInfo>)
        fun PlaylistlinkAdapter(cplayListAdapter: CPlayListAdapter)
        fun PlaylistllinkDataIndex(randomIdx : Int)
        fun PlaylistllinkDataUpdateIndex(randomIdx : Int)

        /**AlbumInfoAdapter*/
        fun AlbumlinkData(datalist: ArrayList<SongInfo>)
        fun AlbumlinkAdapter(albumInfoAdapter: AlbumInfoAdapter)
        fun AlbumlinkDataIndex(idx : Int)
        fun AlbumlinkDataUpdateIndex(idx : Int)

        fun getASongData()
        fun getSongList() :  ArrayList<SongInfo>
        fun getPlayList() :  ArrayList<Int>
        fun loadSetting()
        fun saveData()
        fun getMusicSeekBarNTime()
        fun MusicSeekBarListener() : SeekBar.OnSeekBarChangeListener
        fun onSlideListener(): SlidingUpPanelLayout.PanelSlideListener
        fun slidingPanelLayoutListener(slidingLayout:SlidingUpPanelLayout)

        fun moreBtnClicked()
        fun checkIsPlaying()
        fun playBtnClicked()
        fun nextBtnClicked()
        fun prevBtnClicked()
        fun rotateBtnClicked()
        fun shuffleBtnClicked()
        fun likeBtnClicked()

    }
}
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
        fun moveToFragment(frag: Fragment,key : String?,data:SongInfo)
        fun connectFragment(frag: Fragment)
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

        fun LinkDataList(songInfolist: ArrayList<SongInfo>)
        fun LinkAdapter(Adapter: Any)
        /**SongListAdapter*/
        fun SonglinkDataIndex(idx : Int)
        fun SonglinkDataUpdateIndex(idx : Int)

        /**CPlayListAdapter*/
        fun PlaylistllinkDataIndex(randomIdx : Int)
        fun PlaylistllinkDataUpdateIndex(randomIdx : Int)

        /**AlbumInfoAdapter*/
        fun AlbumlinkDataIndex(idx : Int)

        /**ArtistInfoAdapter*/
        fun ArtistIlinkDataIndex(idx : Int)

        fun getASongData()
        fun getSongList() :  ArrayList<SongInfo>
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
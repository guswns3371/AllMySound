package com.example.allmysound

import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import com.example.allmysound.Base.BaseContract
import com.example.allmysound.Music.SongList.SongListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout

interface MainContract {
    interface View:BaseContract.View{
        override fun showToast(message: String)
        fun setToolbar(toolbar: Toolbar)
        fun connectFragments(view : BottomNavigationView)
        fun setControllerAlpha(distance:Float)
        fun setImageSize(distance:Float)
        fun setBNVHeight(distance:Float)
        fun setBNVSize(distance:Float)

        fun showMoreBtn()
        fun setSongTitle(text:String)
        fun setSongInnerTitle(text:String)
        fun setSongAlbum(text:String)
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

        fun linkData(songInfolist: ArrayList<SongInfo>,songListAdapter: SongListAdapter)
        fun linkDataIndex(idx : Int)
        fun loadData()
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
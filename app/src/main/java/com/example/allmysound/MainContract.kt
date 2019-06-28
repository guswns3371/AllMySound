package com.example.allmysound

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.allmysound.Base.BaseContract
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.net.URI

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

        fun changePlayBtn(isPlay:Boolean)
        fun changeRotateBtn(isRotate:Boolean)
        fun changeShuffleBtn(isShuffle:Boolean)
        fun changeLikeBtn(isLike:Boolean)

    }
    interface Presenter:BaseContract.Presenter<View>{
        override fun setView(view: View)
        override fun releaseView()

        fun linkData(songInfo: SongInfo)
        fun setData()
        fun loadData()
        fun loadSetting()
        fun onSlideListener(): SlidingUpPanelLayout.PanelSlideListener
        fun slidingPanelLayoutListener(slidingLayout:SlidingUpPanelLayout)

        fun moreBtnClicked()
        fun checkIsPlaying()
        fun playBtnClicked()
        fun rotateBtnClicked()
        fun shuffleBtnClicked()
        fun likeBtnClicked()
    }
}
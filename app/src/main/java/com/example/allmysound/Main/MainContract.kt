package com.example.allmysound.Main

import android.widget.SeekBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.allmysound.Base.BaseContract
import com.example.allmysound.Main.Model.SongInfo
import com.sothree.slidinguppanel.SlidingUpPanelLayout

interface MainContract {
    interface View:BaseContract.View{
        override fun connectFragment(frag: Fragment)
        override fun moveToFragment(frag: Fragment,key : String?,data: Any)

        fun setToolbar(toolbar: Toolbar)
        fun setControllerAlpha(distance:Float)
        fun setImageSize(distance:Float)

        fun showMoreBtn()
        fun visibilityLyrics(visibility: Int)
        fun setLyrics(text: String)
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

        fun lyricTxtClicked()
        fun moreBtnClicked()
        fun checkIsPlaying()
        fun playBtnClicked()
        fun nextBtnClicked()
        fun prevBtnClicked()
        fun rotateBtnClicked()
        fun shuffleBtnClicked()
        fun likeBtnClicked()

        fun prepareMusic()
        fun startMusic()
        fun pauseMusic()

    }
}
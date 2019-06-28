package com.example.allmysound

import android.media.MediaPlayer
import android.util.Log
import android.view.View
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.lang.Exception

class MainPresenter:MainContract.Presenter {

    private lateinit var view : MainContract.View
    private lateinit var songInfo: SongInfo
    var mp :  MediaPlayer? = MediaPlayer()

    override fun linkData(songInfo: SongInfo) {
        this.songInfo = songInfo
        Log.e("songInfo  => \n",songInfo.toString())
        setData()
        MainActivity.prefs.setIsPlayingInfo(songInfo)
        try {
            if (mp!!.isPlaying)
                mp!!.reset()
            mp!!.setDataSource(songInfo.file_path)
            mp!!.prepare()
            mp!!.start()
        }catch (e : Exception){
            Log.e("MediaPlayer Exception","$e")
            e.printStackTrace()
        }
    }
    override  fun setData(){
        view.setSongAlbum(songInfo.album)
        view.setSongInnerTitle(songInfo.title)
        view.setSongTitle(songInfo.title)
        view.setSongAlbumArt(songInfo.img)
        view.setSongTime(songInfo.time)
    }

    override fun loadData() {
       val songInfo =  MainActivity.prefs.getIsPlayingInfo()
        if (songInfo != null) {
            view.setSongAlbum(songInfo.album)
            view.setSongInnerTitle(songInfo.title)
            view.setSongTitle(songInfo.title)
            view.setSongAlbumArt(songInfo.img)
            view.setSongTime(songInfo.time)
        }
    }

    override fun loadSetting() {
        view.changeRotateBtn(!MainActivity.prefs.getRotateBoolean())
              mp!!.isLooping = MainActivity.prefs.getRotateBoolean()
        view.changeShuffleBtn(!MainActivity.prefs.getShuffleBoolean())
        view.changeLikeBtn(!MainActivity.prefs.getLikeBoolean())
    }
    override fun setView(view: MainContract.View) {
        this.view = view
    }

    override fun releaseView() {
    }


    override fun moreBtnClicked() {
        view.showToast(songInfo.toString())
    }

    override fun checkIsPlaying() {
        if(mp!!.isPlaying){
            view.changePlayBtn(true)
            MainActivity.prefs.setPlayBoolean(false)
        }else{
            view.changePlayBtn(false)
            MainActivity.prefs.setPlayBoolean(true)
        }
    }

    override fun playBtnClicked() {
        if(MainActivity.prefs.getPlayBoolean()){
            view.changePlayBtn(true)
            MainActivity.prefs.setPlayBoolean(false)
            mp!!.start()
        }else{
            view.changePlayBtn(false)
            MainActivity.prefs.setPlayBoolean(true)
            mp!!.pause()
        }
    }

    override fun rotateBtnClicked() {
        if(MainActivity.prefs.getRotateBoolean()){
            view.changeRotateBtn(true)
            MainActivity.prefs.setRotateBoolean(false)
            mp!!.isLooping = false
        }else{
            view.changeRotateBtn(false)
            MainActivity.prefs.setRotateBoolean(true)
            mp!!.isLooping = true
        }
    }

    override fun shuffleBtnClicked() {
        if(MainActivity.prefs.getShuffleBoolean()){
            view.changeShuffleBtn(true)
            MainActivity.prefs.setShuffleBoolean(false)
        }else{
            view.changeShuffleBtn(false)
            MainActivity.prefs.setShuffleBoolean(true)
        }
    }

    override fun likeBtnClicked(){
        if(MainActivity.prefs.getLikeBoolean()){
            view.changeLikeBtn(true)
            MainActivity.prefs.setLikeBoolean(false)
        }else{
            view.changeLikeBtn(false)
            MainActivity.prefs.setLikeBoolean(true)
        }
    }

    override fun slidingPanelLayoutListener(slidingLayout: SlidingUpPanelLayout) {
        slidingLayout.addPanelSlideListener(onSlideListener())
    }

    override fun onSlideListener(): SlidingUpPanelLayout.PanelSlideListener {
        return object : SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                view.setImageSize(slideOffset)
                view.setControllerAlpha(slideOffset)
            }
            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                if(previousState==SlidingUpPanelLayout.PanelState.COLLAPSED &&
                    newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    //접음->드래그 ->확장
                }else if(previousState==SlidingUpPanelLayout.PanelState.EXPANDED &&
                    newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    //확장->드래그 ->접음
                }
            }
        }
    }

}
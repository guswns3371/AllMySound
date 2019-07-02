package com.example.allmysound

import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainPresenter:MainContract.Presenter {

    private lateinit var view : MainContract.View
    private var songInfo: SongInfo? =null
    private var mp :  MediaPlayer = MediaPlayer()

    override fun linkData(songInfo: SongInfo) {
        this.songInfo = songInfo
        Log.e("songInfo  => \n",songInfo.toString())
        saveData()
        loadData()
        playingMusic()
    }
    override fun saveData() {
        MainActivity.prefs.setIsPlayingInfo(songInfo!!)
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
    fun playingMusic(){
        val songInfo =  MainActivity.prefs.getIsPlayingInfo()
        try {
            mp.reset()
            mp.setDataSource(songInfo!!.file_path)
            mp.prepare()
            mp.start()
            connMusicSeekbar()
            view.setMusicSeekBarListener(MusicSeekBarListener())
        }catch (e : Exception){
            Log.e("MediaPlayer Exception","$e")
            e.printStackTrace()
        }
    }

    override fun MusicSeekBarListener(): SeekBar.OnSeekBarChangeListener {
        return object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) // 이걸 안해주면 계속 mp.seekTo(progress) 코드가 실행되어서 렉 걸림이 심해진다!!
                    mp.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }
    }
    fun connMusicSeekbar() {
        view.setMusicSeekBarProgress(0)
        view.setMusicSeekBarMax(mp.duration)
        view.updateMusicSeekBarNTime()
    }

    override fun getMusicSeekBarNTime(){
        val getCurrent =mp.currentPosition
        val min = TimeUnit.MILLISECONDS.toMinutes(getCurrent.toLong())
        val sec =  TimeUnit.MILLISECONDS.toSeconds(getCurrent.toLong()) - TimeUnit.MINUTES.toSeconds(min)
        val secStr = if(sec<10) "0$sec" else "$sec"
        val time = "$min:$secStr"
        view.setPastTimeText(time)
        view.setMusicSeekBarProgress(getCurrent)
    }

    override fun loadSetting() {
        view.changeRotateBtn(!MainActivity.prefs.getRotateBoolean())
              mp.isLooping = MainActivity.prefs.getRotateBoolean()
        view.changeShuffleBtn(!MainActivity.prefs.getShuffleBoolean())
        view.changeLikeBtn(!MainActivity.prefs.getLikeBoolean())
    }
    override fun setView(view: MainContract.View) {
        this.view = view
    }
    override fun releaseView() {
    }
    override fun checkIsPlaying() {
        if(mp.isPlaying){
            view.changePlayBtn(true)
            MainActivity.prefs.setPlayBoolean(false)
        }else{
            view.changePlayBtn(false)
            MainActivity.prefs.setPlayBoolean(true)
        }
    }

    override fun moreBtnClicked() {
        val songInfo =  MainActivity.prefs.getIsPlayingInfo()
        view.showToast(songInfo.toString())
    }
    override fun playBtnClicked() {
        if(MainActivity.prefs.getPlayBoolean()){
            view.changePlayBtn(true)
            MainActivity.prefs.setPlayBoolean(false)
            if(songInfo==null)
                playingMusic()
            else
                mp.start()
        }else{
            view.changePlayBtn(false)
            MainActivity.prefs.setPlayBoolean(true)
            mp.pause()
        }
    }
    override fun rotateBtnClicked() {
        if(MainActivity.prefs.getRotateBoolean()){
            view.changeRotateBtn(true)
            MainActivity.prefs.setRotateBoolean(false)
            mp.isLooping = false
        }else{
            view.changeRotateBtn(false)
            MainActivity.prefs.setRotateBoolean(true)
            mp.isLooping = true
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
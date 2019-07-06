package com.example.allmysound.Main

import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.SongList.SongListAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainPresenter: MainContract.Presenter {

    private lateinit var view : MainContract.View
    private lateinit var songListAdapter :SongListAdapter
    private var songInfolist: ArrayList<SongInfo>? =null
    private var numList: ArrayList<Int>? = null
//    private var songInfo: SongInfo? =null
    private var isNewState : Boolean = true
    private var idx: Int =0
    private var randomIdx: Int =-1
    private var mp :  MediaPlayer = MediaPlayer()

    override fun getSongList(): ArrayList<SongInfo> = songInfolist!!
    override fun getPlayList() :  ArrayList<Int> = numList!!

    override fun linkData(songInfolist:  ArrayList<SongInfo>, songListAdapter: SongListAdapter) {
        this.songInfolist = songInfolist
        this.songListAdapter = songListAdapter
        numList = getNumList()
    }
    override fun linkDataIndex(idx: Int) {
        this.idx = idx
        SaveLoadPrepareStart()
        isNewState= false
    }

    private fun SaveLoadPrepareStart(){
        saveData()
        loadData()
        prepareMusic()
        startMusic()
    }
    private fun SaveLoadPrepare(){
        saveData()
        loadData()
        prepareMusic()
    }
    override fun saveData() {
        MainActivity.prefs.setIsPlayingInfo(songInfolist!![idx])
    }
    override fun loadData() {
        val songInfo =  MainActivity.prefs.getIsPlayingInfo()
        if (songInfo != null) {
            view.setSongAlbum(songInfo.album)
            view.setSongArtist(songInfo.artist)
            view.setSongInnerTitle(songInfo.title)
            view.setSongTitle(songInfo.title)
            view.setSongAlbumArt(songInfo.img)
            view.setSongTime(songInfo.time)
            this.idx = songInfo.orderNum
        }
    }
    private fun startMusic(){
        try {
            mp.start()
            mp.setOnCompletionListener {
                nextBtnClicked()
                mp.reset()
                mp.setDataSource(songInfolist!![idx++].file_path)
                mp.prepare()
                connMusicSeekbar()
                mp.start()
            }
        }catch (e : Exception){
            Log.e("MediaPlayer Exception","$e")
            e.printStackTrace()
        }
    }
    private fun prepareMusic(){
        try {
            mp.reset()
            mp.setDataSource(songInfolist!![idx].file_path)
            mp.prepare()
            connMusicSeekbar() // mp.prepare() 이전에 위치하면 클남
            view.setMusicSeekBarListener(MusicSeekBarListener())
            mp.isLooping = MainActivity.prefs.getRotateBoolean()
        }catch (e : Exception){
            Log.e("MediaPlayer Exception","$e")
            e.printStackTrace()
        }
    }
    private fun pauseMusic(){
        mp.pause()
    }

    private fun nextMusic(){
        view.setMusicSeekBarProgress(0)

        if(MainActivity.prefs.getShuffleBoolean()){
            randomIdx++
            if(randomIdx == numList!!.size)
                randomIdx =0
            idx = numList!![randomIdx]
        }else{
            randomIdx =-1
            idx++
            if(idx == songInfolist!!.size)
                idx =0
        }
        if(mp.isPlaying)
            SaveLoadPrepareStart()
        else{
            SaveLoadPrepare()
        }
    }
    private fun prevMusic(){
        if(MainActivity.prefs.getShuffleBoolean()){
            randomIdx--
            if(randomIdx<=-1)
                randomIdx = numList!!.size-1
            idx = numList!![randomIdx]
        }else{
            randomIdx = -1
            idx--
            if(idx <0)
                idx =songInfolist!!.size-1
        }
        if(mp.isPlaying)
            SaveLoadPrepareStart()
        else{
            SaveLoadPrepare()
        }
    }
    private fun getNumList() : ArrayList<Int>{
        val numbers : ArrayList<Int> = ArrayList()
        val min = 0
        val max = songInfolist!!.size-1
        for(i in min..max) numbers.add(i)

        if(MainActivity.prefs.getShuffleBoolean())
            numbers.shuffle()
        return numbers
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
        view.changeRotateBtn(MainActivity.prefs.getRotateBoolean())
        view.changeShuffleBtn(MainActivity.prefs.getShuffleBoolean())
        view.changeLikeBtn(MainActivity.prefs.getLikeBoolean())
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
        Log.e("moreBtnClicked ",songInfo.toString())
        view.showMoreBtn(songInfo!!)
    }
    override fun playBtnClicked() {
        if(MainActivity.prefs.getPlayBoolean()){
            view.changePlayBtn(true)
            MainActivity.prefs.setPlayBoolean(false)
            if(isNewState){
                prepareMusic()
                startMusic()
            }else{
                startMusic()
            }
        }else{
            view.changePlayBtn(false)
            MainActivity.prefs.setPlayBoolean(true)
            pauseMusic()
        }
    }
    override fun nextBtnClicked() {
        nextMusic()
        songListAdapter.notifyDataSetChanged()
    }
    override fun prevBtnClicked() {
        prevMusic()
        songListAdapter.notifyDataSetChanged()
    }
    override fun rotateBtnClicked() {
        if(MainActivity.prefs.getRotateBoolean()){
            view.changeRotateBtn(false)
            MainActivity.prefs.setRotateBoolean(false)
            mp.isLooping = false
        }else{
            view.changeRotateBtn(true)
            MainActivity.prefs.setRotateBoolean(true)
            mp.isLooping = true
        }
    }
    override fun shuffleBtnClicked() {
        if(MainActivity.prefs.getShuffleBoolean()){
            view.changeShuffleBtn(false)
            MainActivity.prefs.setShuffleBoolean(false)
        }else{
            view.changeShuffleBtn(true)
            MainActivity.prefs.setShuffleBoolean(true)
        }
        numList = getNumList()
        randomIdx =-1
    }
    override fun likeBtnClicked(){
        if(MainActivity.prefs.getLikeBoolean()){
            view.changeLikeBtn(false)
            MainActivity.prefs.setLikeBoolean(false)
        }else{
            view.changeLikeBtn(true)
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
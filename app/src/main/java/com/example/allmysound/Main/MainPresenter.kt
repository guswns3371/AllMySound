package com.example.allmysound.Main

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.example.allmysound.Base.BasicListFrag.BasicListAdapter
import com.example.allmysound.Extensions.getPreference
import com.example.allmysound.Main.Dialog.Adapter.CPlayListAdapter
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Music.InfoPage.AlbumInfo.Adapter.AlbumInfoAdapter
import com.example.allmysound.Music.InfoPage.ArtistInfo.AritstInfo.ArtistInfoAdapter
import com.example.allmysound.Music.SongList.Adapter.SongListAdapter
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainPresenter(private val context: Context): MainContract.Presenter {

    private lateinit var view : MainContract.View

    private var basicListAdapter : BasicListAdapter? = null
    private var songListAdapter : SongListAdapter? = null
    private var cplayListAdapter :CPlayListAdapter? = null
    private var albumInfoAdapter :AlbumInfoAdapter? = null
    private var artistInfoAdapter :ArtistInfoAdapter? = null

    private var songInfolist: ArrayList<SongInfo>? =null
    private var numList: ArrayList<Int>? =  null
    var isNewState : Boolean = true
    private var songlistIdx: Int =0
    private var randomIdx: Int =-1
    private var mp :  MediaPlayer = MediaPlayer()

    override fun getSongList(): ArrayList<SongInfo> = songInfolist!!

    override fun LinkDataList(songInfolist: ArrayList<SongInfo>) {
        this.songInfolist = songInfolist
    }
    override fun LinkAdapter(Adapter: Any) {
        when(Adapter){
            is SongListAdapter ->{
                this.songListAdapter = Adapter
                numList = getNumList()
            }
            is CPlayListAdapter ->{this.cplayListAdapter = Adapter}
            is AlbumInfoAdapter -> {this.albumInfoAdapter = Adapter}
            is ArtistInfoAdapter ->{this.artistInfoAdapter = Adapter}
            is BasicListAdapter ->{this.basicListAdapter = Adapter}
            else -> throw IllegalArgumentException()
        }
    }
    /**SongListAdapter*/
    override fun SonglinkDataIndex(idx: Int) {
        this.songlistIdx = idx
        SaveLoadPrepareStart()
        isNewState= false
    }
    override fun SonglinkDataUpdateIndex(idx: Int) {
        this.songlistIdx = idx
    }

    /**CPlayListAdapter*/
    override fun PlaylistllinkDataIndex(randomIdx: Int) {
        this.randomIdx = randomIdx
        val numList = context.getPreference().getPlayListInt()!!
        songlistIdx = numList[randomIdx]
        SaveLoadPrepareStart()
        songListAdapter!!.notifyDataSetChanged()
    }
    override fun PlaylistllinkDataUpdateIndex(randomIdx: Int) {
        this.randomIdx = randomIdx
    }

    /**AlbumInfoAdapter*/
    override fun AlbumlinkDataIndex(idx: Int) {
        this.songlistIdx = idx
        SaveLoadPrepareStart()
    }

    /**ArtistInfoAdapter*/
    override fun ArtistIlinkDataIndex(idx: Int) {
        this.songlistIdx = idx
        SaveLoadPrepareStart()
    }

    /**********************************************************/
    private fun SaveLoadPrepareStart(){
        saveData()
        getASongData()
        prepareMusic()
        startMusic()
    }
    private fun SaveLoadPrepare(){
        saveData()
        getASongData()
        prepareMusic()
    }
    override fun saveData() {
        context.getPreference().setIsPlayingInfo(songInfolist!![songlistIdx])
    }
    override fun getASongData() {
        val songInfo =  context.getPreference().getIsPlayingInfo()
        if (songInfo != null) {
            this.songlistIdx = songInfo.orderNum
            view.setSongAlbum(songInfo.album)
            view.setSongArtist(songInfo.artist)
            view.setSongInnerTitle(songInfo.title)
            view.setSongTitle(songInfo.title)
            view.setSongAlbumArt(songInfo.img)
            view.setSongTime(songInfo.time)

            view.setLyrics(getLyrics())
            view.visibilityLyrics(View.GONE)
        }
    }
    override fun startMusic(){
        try {
            mp.start()
            mp.setOnCompletionListener {
                nextBtnClicked()
                mp.reset()
                mp.setDataSource(songInfolist!![songlistIdx++].file_path)
                mp.prepare()
                connMusicSeekbar()
                mp.start()

                notifyAdapters()
            }
        }catch (e : Exception){
            Log.e("MediaPlayer Exception","$e")
            e.printStackTrace()
        }
    }
    override fun prepareMusic(){
        try {
            mp.reset()
            mp.setDataSource(songInfolist!![songlistIdx].file_path)
            mp.prepare()
            connMusicSeekbar() // mp.prepare() 이전에 위치하면 클남
            view.setMusicSeekBarListener(MusicSeekBarListener())
            mp.isLooping = context.getPreference().getRotateBoolean()
        }catch (e : Exception){
            Log.e("MediaPlayer Exception","$e")
            e.printStackTrace()
        }
    }
    override fun pauseMusic(){
        mp.pause()
    }

    private fun nextMusic(){
        view.setMusicSeekBarProgress(0)
        val numList = context.getPreference().getPlayListInt()!!
        if(context.getPreference().getShuffleBoolean()){
            randomIdx++
            if(randomIdx == numList.size)
                randomIdx =0
            songlistIdx = numList[randomIdx]
        }else{
            randomIdx =-1
            songlistIdx++
            if(songlistIdx == songInfolist!!.size)
                songlistIdx =0
        }
        if(mp.isPlaying)
            SaveLoadPrepareStart()
        else{
            SaveLoadPrepare()
        }
    }
    private fun prevMusic(){
        val numList = context.getPreference().getPlayListInt()!!
        if(context.getPreference().getShuffleBoolean()){
            randomIdx--
            if(randomIdx<=-1)
                randomIdx = numList.size-1
            songlistIdx = numList[randomIdx]
        }else{
            randomIdx = -1
            songlistIdx--
            if(songlistIdx <0)
                songlistIdx =songInfolist!!.size-1
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

        if(context.getPreference().getShuffleBoolean())
            numbers.shuffle()

        context.getPreference().setPlayListInt(numbers)
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
        view.changeRotateBtn(context.getPreference().getRotateBoolean())
        view.changeShuffleBtn(context.getPreference().getShuffleBoolean())
        view.changeLikeBtn(context.getPreference().getLikeBoolean())
    }
    override fun setView(view: MainContract.View) {
        this.view = view
    }
    override fun releaseView() {
    }
    override fun checkIsPlaying() {
        if(mp.isPlaying){
            view.changePlayBtn(true)
            context.getPreference().setPlayBoolean(false)
        }else{
            view.changePlayBtn(false)
            context.getPreference().setPlayBoolean(true)
        }
    }

    override fun lyricTxtClicked() {
        if(context.getPreference().getIsPlayingInfo() == null) return
        if(getLyrics()=="")
            view.visibilityLyrics(View.GONE)
        else
            view.visibilityLyrics(View.VISIBLE)
        view.setLyrics(getLyrics())
    }
    override fun moreBtnClicked() {
        if(context.getPreference().getIsPlayingInfo() == null) return
        Log.e("moreBtnClicked ",context.getPreference().getIsPlayingInfo().toString())
        view.showMoreBtn()
    }
    override fun playBtnClicked() {
        if(context.getPreference().getIsPlayingInfo() == null) return
        if(context.getPreference().getPlayBoolean()){
            view.changePlayBtn(true)
            context.getPreference().setPlayBoolean(false)
            if(isNewState){
                prepareMusic()
                startMusic()
            }else{
                startMusic()
            }
        }else{
            view.changePlayBtn(false)
            context.getPreference().setPlayBoolean(true)
            pauseMusic()
        }
    }
    override fun nextBtnClicked() {
        if(context.getPreference().getIsPlayingInfo() == null) return
        nextMusic()
        notifyAdapters()
    }
    override fun prevBtnClicked() {
        if(context.getPreference().getIsPlayingInfo() == null) return
        prevMusic()
        notifyAdapters()
    }
    override fun rotateBtnClicked() {
        if(context.getPreference().getRotateBoolean()){
            view.changeRotateBtn(false)
            context.getPreference().setRotateBoolean(false)
            mp.isLooping = false
        }else{
            view.changeRotateBtn(true)
            context.getPreference().setRotateBoolean(true)
            mp.isLooping = true
        }
    }
    override fun shuffleBtnClicked() {
        if(context.getPreference().getShuffleBoolean()){
            view.changeShuffleBtn(false)
            context.getPreference().setShuffleBoolean(false)
        }else{
            view.changeShuffleBtn(true)
            context.getPreference().setShuffleBoolean(true)
        }

        setRandomIdxNumList()
    }
    override fun likeBtnClicked(){
        if(context.getPreference().getLikeBoolean()){
            view.changeLikeBtn(false)
            context.getPreference().setLikeBoolean(false)
        }else{
            view.changeLikeBtn(true)
            context.getPreference().setLikeBoolean(true)
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
                if(slideOffset!=1.0f)
                    view.visibilityLyrics(View.GONE)
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

    private fun notifyAdapters(){
        if(songListAdapter!=null)
            songListAdapter!!.notifyDataSetChanged()
        if(cplayListAdapter!=null)
            cplayListAdapter!!.notifyDataSetChanged()
        if(albumInfoAdapter!=null)
            albumInfoAdapter!!.myNotifyDataSetChanged()
        if(artistInfoAdapter!=null)
            artistInfoAdapter!!.myNotifyDataSetChanged()
        if(basicListAdapter!=null)
            basicListAdapter!!.notifyDataSetChanged()
    }

    fun setRandomIdxNumList(){
            numList = getNumList()
            randomIdx = numList!!.indexOf(context.getPreference().getIsPlayingInfo()!!.orderNum)
    }
    private fun getLyrics(): String{
        val songInfo =  context.getPreference().getIsPlayingInfo()
        val mp3 = AudioFileIO.read(File(songInfo!!.file_path))
        val value = mp3.tag.getFirst(FieldKey.LYRICS)
        return String(value.toByteArray(StandardCharsets.UTF_8),StandardCharsets.UTF_8)
    }
}
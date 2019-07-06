package com.example.allmysound.Main
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.allmysound.Music.MusicFrag
import com.example.allmysound.Recommend.RecommendFrag
import com.example.allmysound.Search.SearchFrag
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.music_control.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.music_playing.*
import android.widget.SeekBar
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.example.allmysound.Main.Dialog.MoreCustomDialog
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.Main.Pref.MySharedPreference
import com.example.allmysound.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), MainContract.View {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var prefs: MySharedPreference

        var presenter: MainPresenter = MainPresenter()
        fun createMainPresenter() : MainPresenter =
            presenter
    }

    private lateinit var customDialog : MoreCustomDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        blurry()
        prefs = MySharedPreference(this)
        Permission()
        presenterSetting()
        setToolbar(main_toolbar)
        connectFragments(BtmNavView)
        initVolumeControl()
        textviewScrolling()
        setClickListners()
    }

    override fun setMusicSeekBarMax(max: Int) {
        music_seekbar.max = max
    }
    override  fun setMusicSeekBarProgress(progress: Int){
        music_seekbar.progress = progress
    }
    override fun setMusicSeekBarListener(listener : SeekBar.OnSeekBarChangeListener){
        music_seekbar.setOnSeekBarChangeListener(listener)
    }
    override fun setPastTimeText(text: String) {
        past_time.text = text
    }
    override fun updateMusicSeekBarNTime() {
        Thread(object: Runnable {
            override fun run() {
                presenter.getMusicSeekBarNTime()
                Handler().postDelayed(this,1000)
            }
        }).run()

    }

    fun setClickListners(){
        dragView.setOnClickListener {
            if (sliding_layout.panelState !=SlidingUpPanelLayout.PanelState.EXPANDED
                && sliding_layout.panelState !=SlidingUpPanelLayout.PanelState.ANCHORED){
                if (sliding_layout.anchorPoint < 1.0f)
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                else
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
        }
        ctr_play.setOnClickListener { presenter.playBtnClicked() }
        ctr_next.setOnClickListener {  presenter.nextBtnClicked() }
        music_control.setOnLongClickListener { showToast("music_control");true }
        music_control.setOnClickListener { sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED }
        ctr_cover_cv.setOnClickListener { showToast("ctr_cover") }
        song_album.setOnClickListener { showToast("song_album") }
        song_artist.setOnClickListener { showToast("song_artist") }
        more.setOnClickListener { presenter.moreBtnClicked() }
        play_pre.setOnClickListener { presenter.prevBtnClicked() }
        play.setOnClickListener { presenter.playBtnClicked()}
        play_next.setOnClickListener { presenter.nextBtnClicked()}
        shuffle.setOnClickListener { presenter.shuffleBtnClicked()}
        like.setOnClickListener { presenter.likeBtnClicked() }
        rotate.setOnClickListener { presenter.rotateBtnClicked() }
    }
    fun textviewScrolling(){
        ctr_name.isSelected =true
        song_title_.isSelected =true
        song_album.isSelected =true
    }
    fun presenterSetting(){
        presenter.setView(this)
        presenter.slidingPanelLayoutListener(sliding_layout)
        presenter.loadData()
        presenter.loadSetting()

    }
    fun Permission(){
        val permissionlistener = object : PermissionListener {
            override fun onPermissionGranted() {
                Log.e("Songlist_Permission", "Permission Granted")
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Log.e("Songlist_Permission", "Permission Denied$deniedPermissions")
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("퍼미션 거부시 ,서비스를 이용 할 수 없습니다\n\n설정에서 퍼미션을 승인하세요 ")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }
    fun blurry(){
        val radius = 25f
        val decorView = window.decorView
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        val rootView = decorView.findViewById(android.R.id.content) as ViewGroup
        //set background, if your root layout doesn't have one
        val windowBackground = decorView.background

        blurryView.setupWith(rootView)
            .windowBackground(windowBackground)
            .blurAlgorithm(SupportRenderScriptBlur(this))
            .blurRadius(radius)
            .setHasFixedTransformationMatrix(true)

    }
    fun initVolumeControl(){
        val am : AudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volume_seekbar.max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volume_seekbar.progress = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        volume_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        } )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean { // 볼륨버튼으로 seekbar 조절
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                volume_seekbar.progress -=1
                return true
            }
            KeyEvent.KEYCODE_BACK -> return true
        }

        return false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                volume_seekbar.progress +=1
                return true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                this.finish()
                return true
            }
        }
        return false
    }

    override fun setSongTitle(text:String) {
        ctr_name.text = text
    }
    override fun setSongInnerTitle(text:String) {
        song_title_.text = text
    }
    override fun setSongAlbum(text:String) {
        song_album.text = text
    }

    override fun setSongArtist(text: String) {
        song_artist.text = text
    }
    @SuppressLint("SetTextI18n")
    override fun setSongTime(text:String) {
        val timeLong = text.toLong()
        val min = TimeUnit.MILLISECONDS.toMinutes(timeLong)
        val sec = TimeUnit.MILLISECONDS.toSeconds(timeLong) - min*60
        val secStr = if (sec<10)  "0$sec" else  "$sec"
        remain_time.text = "$min:$secStr"
    }
    override fun setSongAlbumArt(uri: String){
        Picasso.get()
            .load(uri.toUri())
            .error(R.drawable.song_500)
            .into(ctr_cover)
    }

    override fun showMoreBtn(songInfo: SongInfo){
        customDialog= MoreCustomDialog(this)

        customDialog.setSongList(presenter.getSongList())
        customDialog.setNumList(presenter.getPlayList())
        customDialog.setPlayingIdx(MainActivity.prefs.getIsPlayingInfo()!!.orderNum)

        customDialog.mSetData = object : MoreCustomDialog.SetData {
            override fun setImage(): String = MainActivity.prefs.getIsPlayingInfo()!!.img
            override fun setTitle(): String  =  MainActivity.prefs.getIsPlayingInfo()!!.title
            override fun setArtist(): String =  MainActivity.prefs.getIsPlayingInfo()!!.artist
            override fun setAlbum(): String =  MainActivity.prefs.getIsPlayingInfo()!!.album
        }
        customDialog.mClickListener = object: MoreCustomDialog.ClickListener {
            override fun clickInfo() {
                showToast("clickInfo")
            }

            override fun clickPlaylist() {
                customDialog.showPlayList()
            }

            override fun clickPlaylistAdd() {
                customDialog.showExistingPlayList()
            }

            override fun clickDelete() {
                showToast("clickDelete")
            }

            override fun clickCancel() {
                customDialog.cancel()
            }

            override fun clickShuffle() {
                MainActivity.prefs.setShuffleBoolean(false)
                presenter.shuffleBtnClicked()
                customDialog.setNumList(presenter.getPlayList())
                customDialog.setPlayingIdx(MainActivity.prefs.getIsPlayingInfo()!!.orderNum)
            }

            override fun clickUnshuffle() {
                MainActivity.prefs.setShuffleBoolean(true)
                presenter.shuffleBtnClicked()
                customDialog.setNumList(presenter.getPlayList())
                customDialog.setPlayingIdx(MainActivity.prefs.getIsPlayingInfo()!!.orderNum)
            }
        }
//        customDialog.setCancelable(false)
        customDialog.show()
        //다이얼로그 크기 , 위치, 모양
        customDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.window?.setGravity(Gravity.BOTTOM)
        customDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    override fun changePlayBtn(isPlay:Boolean){
        if (isPlay){
            play.setImageResource(R.drawable.pause)
            ctr_play.setImageResource(R.drawable.pause)
        }else{
            play.setImageResource(R.drawable.play)
            ctr_play.setImageResource(R.drawable.play)
        }

    }
    override fun changeRotateBtn(isRotate:Boolean){
        if (isRotate)
            rotate.setImageResource(R.drawable.rotate_one)
        else
            rotate.setImageResource(R.drawable.rotate_all)
    }
    override fun changeShuffleBtn(isShuffle:Boolean){
        if (isShuffle)
            shuffle.setImageResource(R.drawable.shuffle)
        else
            shuffle.setImageResource(R.drawable.unshuffle)
    }
    override fun changeLikeBtn(isLike:Boolean){
        if (isLike)
            like.setImageResource(R.drawable.like)
        else
            like.setImageResource(R.drawable.unlike)
    }

    override fun showToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    override fun connectFragments(view : BottomNavigationView){
        view.setOnNavigationItemSelectedListener {
            var selectedFrag : Fragment? =null
            when(it.itemId){
                R.id.menu_Music -> selectedFrag = MusicFrag()
                R.id.menu_Recommended -> selectedFrag = RecommendFrag()
                R.id.menu_Search -> selectedFrag = SearchFrag()
            }
            supportFragmentManager.beginTransaction().replace(R.id.frag_container,selectedFrag!!).commit()
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.frag_container, MusicFrag()).commit()
    }
    override fun setToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar!!.title = ""
        actionbar.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_Music -> {
                supportFragmentManager.beginTransaction().replace(R.id.frag_container,MusicFrag()).commit()
                return true
            }
            R.id.menu_Recommended -> {
                supportFragmentManager.beginTransaction().replace(R.id.frag_container,RecommendFrag()).commit()
                return true
            }
            R.id.menu_Search -> {
                supportFragmentManager.beginTransaction().replace(R.id.frag_container,SearchFrag()).commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setBNVHeight(distance:Float){
        val originalHeight = 196
        val height= (originalHeight.toFloat())-( (originalHeight.toFloat())*((10000.0f-distance*10000.0f)/10000.0f) )
        BtmNavView.animate()
            .translationY(height)
            .duration=0
    }
    override fun setBNVSize(distance:Float){
        val originalHeight = 196
        BtmNavView.requestLayout()
        BtmNavView.layoutParams.height =  ( (originalHeight.toFloat())*((10000.0f-distance*10000.0f)/10000.0f) ).toInt()
    }
    override fun setControllerAlpha(distance:Float){
        val alphaValue = (10000.0f-distance*40000.0f)
        music_control_two.animate()
            .alpha(alphaValue/10000.0f)
            .duration = 0
    }
    override fun setImageSize(distance:Float){
//        Log.e("ctr_cover_cv.layoutParams.height ","${ctr_cover_cv.layoutParams.height}")
        val originalHeight = 210
        val marginParams = ctr_cover_cv.layoutParams as MarginLayoutParams
        ctr_cover_cv.requestLayout()
        ctr_cover_cv.layoutParams.height = ((distance*820.0f).toInt()+originalHeight)
        ctr_cover_cv.layoutParams.width = ((distance*820.0f).toInt()+originalHeight)
        marginParams.setMargins(
            (distance*180.0f).toInt(),(distance*140.0f).toInt(),
            (distance*180.0f).toInt(),(distance*75.0f).toInt()  )
    }
}
package com.example.allmysound.Base.Service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.allmysound.Base.App.App.Companion.CHANNEL_ID
import com.example.allmysound.Base.Extensions.showToast
import com.example.allmysound.R
import android.content.IntentFilter
import android.content.Intent
import androidx.core.net.toUri
import com.example.allmysound.Base.App.App.Companion.DATALIST
import com.example.allmysound.Base.App.App.Companion.POSITON
import com.example.allmysound.Base.Extensions.getPreference
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.MainPresenter
import com.example.allmysound.Main.Model.SongInfo

class MusicService : Service() {

    companion object{
        const val MUSIC_SERVICE_FILTER = "MUSIC_SERVICE_FILTER"
        const val  MUSIC_PREV = "MUSIC_PREV"
        const val  MUSIC_NOW = "MUSIC_NOW"
        const val  MUSIC_NEXT = "MUSIC_NEXT"
        const val  MUSIC_CLOSE = "MUSIC_CLOSE"
    }
    private lateinit var datalist : ArrayList<SongInfo>
    private var nowPosition = 0
    private lateinit var mp : MediaPlayer


    private lateinit var presenter : MainPresenter
    private lateinit var notification : Notification
    private lateinit var contentView : RemoteViews
    private lateinit var filter : IntentFilter
    private val btnReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent!!.action){
                MUSIC_PREV ->{
                }
                MUSIC_NOW ->{
                }
                MUSIC_NEXT ->{
                }
                MUSIC_CLOSE ->{
                    stopForeground(true)
                    sendBroadcast(Intent(this@MusicService,MainActivity::class.java))
                }
            }
        }
    }
    private val audioFocusChangeListener = object  : AudioManager.OnAudioFocusChangeListener{
        override fun onAudioFocusChange(focusChange: Int) {
        }
    }


    override fun onBind(intent: Intent): IBinder {
        Log.e("MusicService","onBind")
        TODO("Return the communication channel to the service.")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("MusicService","onUnbind")
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        Log.e("MusicService","onCreate")
        mp = MediaPlayer()
        datalist = ArrayList()
        presenter = MainActivity.createMainPresenter()
        filter = IntentFilter()

        filter.addAction(MUSIC_PREV)
        filter.addAction(MUSIC_NOW)
        filter.addAction(MUSIC_NEXT)
        filter.addAction(MUSIC_CLOSE)
        registerReceiver(btnReceiver, filter)

        super.onCreate()
    }

    override fun onDestroy() {
        Log.e("MusicService","onDestroy")
        unregisterReceiver(btnReceiver)
        mp.release()
        stopForeground(true)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("MusicService","onStartCommand")
        getIntentData(intent!!)
        creatNotification()
        startForeground(123, notification)

        return START_STICKY
    }
    override fun startForegroundService(service: Intent?): ComponentName? {
        Log.e("MyService","startForegroundService")
        return super.startForegroundService(service)
    }


    private fun getIntentData(intent: Intent){
        nowPosition = intent.extras!!.getInt(POSITON)
        datalist = intent.getParcelableArrayListExtra(DATALIST)
    }
    private fun creatNotification(){
        val notiIntent = Intent(this@MusicService,MainActivity::class.java)
        notiIntent.putExtra("MusicService",true)
        val pendingIntent = PendingIntent.getActivity(this,0,
            notiIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        contentView = RemoteViews(packageName, R.layout.notification_small)

        val prevIntent = Intent(MUSIC_PREV)
        val nowIntent = Intent(MUSIC_NOW)
        val nextIntent = Intent(MUSIC_NEXT)
        val closeIntent = Intent(MUSIC_CLOSE)

        val pdIntentPrev = PendingIntent.getBroadcast(this, 0, prevIntent, 0)
        val pdIntentNow = PendingIntent.getBroadcast(this, 0, nowIntent, 0)
        val pdIntentNext = PendingIntent.getBroadcast(this, 0, nextIntent, 0)
        val pdIntentClose = PendingIntent.getBroadcast(this, 0, closeIntent, 0)

        contentView.setOnClickPendingIntent(R.id.pre_btn,pdIntentPrev)
        contentView.setOnClickPendingIntent(R.id.pause_btn,pdIntentNow)
        contentView.setOnClickPendingIntent(R.id.next_btn,pdIntentNext)
        contentView.setOnClickPendingIntent(R.id.cancel_btn,pdIntentClose)

        /** 노티의 이미지뷰에 리소스를 지정해줘야한다. + cardview가 있으면 노티가 생성되지 않는다.*/
        contentView.setImageViewResource(R.id.pre_btn,R.drawable.play_pre)
        if(getPreference().getPlayBoolean())
            contentView.setImageViewResource(R.id.pause_btn,R.drawable.pause)
        else
            contentView.setImageViewResource(R.id.pause_btn,R.drawable.play)
        contentView.setImageViewResource(R.id.next_btn,R.drawable.play_next)
        contentView.setImageViewResource(R.id.cancel_btn,R.drawable.cancel)
        contentView.setImageViewResource(R.id.music_logo,R.drawable.music_icon)
        contentView.setTextViewText(R.id.title_txt,datalist[nowPosition].title)
        contentView.setTextViewText(R.id.artist_txt,datalist[nowPosition].artist)
        contentView.setImageViewUri(R.id.cover_img,datalist[nowPosition].img.toUri())

        notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.music_icon)
            .setContentIntent(pendingIntent)
            .setContent(contentView)
            .setVibrate(longArrayOf(0))
            .build()
    }

    private fun changeNotiInfo(){
        contentView.setTextViewText(R.id.title_txt,datalist[nowPosition].title)
        contentView.setTextViewText(R.id.artist_txt,datalist[nowPosition].artist)
        contentView.setImageViewUri(R.id.cover_img,datalist[nowPosition].img.toUri())
        notification.contentView = contentView  //???

        startForeground(123, notification)
    }

    private fun musicOn(pos : Int){

    }
}

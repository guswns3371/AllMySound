package com.example.allmysound.Service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import com.example.allmysound.Main.MainPresenter

class MusicService : Service() {

    val MESSAGE_KEY = "MusicService"
    val MUSIC_PREV = "MUSIC_PREV"
    val MUSIC_NOW = "MUSIC_NOW"
    val MUSIC_NEXT = "MUSIC_NEXT"
    val MUSIC_CLOSE = "MUSIC_CLOSE"
    var message : Boolean? =null

    private lateinit var filter : IntentFilter
    private lateinit var mainPresenter : MainPresenter
    private val receiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent!!.action){
                MUSIC_PREV ->{

                }
                MUSIC_NOW ->{

                }
                MUSIC_NEXT ->{

                }
                MUSIC_CLOSE ->{

                }
            }
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
        filter = IntentFilter()
        filter.addAction(MUSIC_PREV)
        filter.addAction(MUSIC_NOW)
        filter.addAction(MUSIC_NEXT)
        filter.addAction(MUSIC_CLOSE)

        registerReceiver(receiver,filter)
        super.onCreate()
    }

    override fun onDestroy() {
        Log.e("MusicService","onDestroy")
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("MusicService","onStartCommand")
        message = intent?.extras!!.getBoolean(this.MESSAGE_KEY)
        when(message){
            true ->{}
            false ->{}
        }
        return super.onStartCommand(intent, flags, startId)
    }
}

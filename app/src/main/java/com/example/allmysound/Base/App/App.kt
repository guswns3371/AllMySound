package com.example.allmysound.Base.App

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log

class App : Application() {
    companion object {
        const val CHANNEL_ID  = "com.example.allmysound"
        const val POSITON = "POSITON"
        const val DATALIST = "DATALIST"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.e("App","createNotificationChannel")
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "all my sound",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationChannel.enableLights(false)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(false)
            notificationChannel.setSound(null,null)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }
}
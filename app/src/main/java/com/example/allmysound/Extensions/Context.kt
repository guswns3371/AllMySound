package com.example.allmysound.Extensions

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.prefill.PreFillType
import com.bumptech.glide.util.Util.isOnMainThread
import com.example.allmysound.Main.Pref.MySharedPreference

fun Context.getPreference() : MySharedPreference{
    return MySharedPreference(this)
}

fun Context.showToast(msg:String, length: Int = Toast.LENGTH_SHORT){
    toast(msg,length)
}

fun Context.toast(id: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), length)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        if (isOnMainThread()) {
            doToast(this, msg, length)
        } else {
            Handler(Looper.getMainLooper()).post {
                doToast(this, msg, length)
            }
        }
    } catch (e: Exception) {
    }
}

private fun doToast(context: Context, message: String, length: Int) {
    if (context is Activity) {
        if (!context.isFinishing && !context.isDestroyed) {
            Toast.makeText(context, message, length).show()
        }
    } else {
        Toast.makeText(context, message, length).show()
    }
}
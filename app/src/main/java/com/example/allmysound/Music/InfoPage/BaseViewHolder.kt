package com.example.allmysound.Music.InfoPage

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.allmysound.Main.Model.SongInfo

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: SongInfo, pos: Int, context: Context)
}
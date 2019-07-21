package com.example.allmysound.Music.PlayList

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.allmysound.Base.BaseContract
import com.example.allmysound.Main.Model.SongInfo

interface PlayListContract {
    interface View:BaseContract.View{
        override fun connectFragment(frag: Fragment)
        override fun moveToFragment(frag: Fragment, key: String?, data: Any)
    }
    interface Presenter:BaseContract.Presenter<View>{
        override fun setView(view: View)
        override fun releaseView()
    }
}
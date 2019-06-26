package com.example.allmysound

import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.allmysound.Base.BaseContract
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout

interface MainContract {
    interface View:BaseContract.View{
        override fun showToast(message: String)
        fun setToolbar(toolbar: Toolbar)
        fun connectFragments(view : BottomNavigationView)
        fun setControllerAlpha(distance:Float)
        fun setImageSize(distance:Float)
        fun setBNVHeight(distance:Float)
        fun setBNVSize(distance:Float)
    }
    interface Presenter:BaseContract.Presenter<View>{
        override fun setView(view: View)
        override fun releaseView()
        fun onSlideListener(): SlidingUpPanelLayout.PanelSlideListener
        fun slidingPanelLayoutListener(slidingLayout:SlidingUpPanelLayout)
    }
}
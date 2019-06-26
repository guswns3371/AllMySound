package com.example.allmysound

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MainPresenter:MainContract.Presenter {

    private lateinit var view : MainContract.View

    override fun setView(view: MainContract.View) {
        this.view = view
    }

    override fun releaseView() {
    }

    override fun slidingPanelLayoutListener(slidingLayout: SlidingUpPanelLayout) {
        slidingLayout.addPanelSlideListener(onSlideListener())
    }

    override fun onSlideListener(): SlidingUpPanelLayout.PanelSlideListener {
        return object : SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                Log.e("onPanelSlide","onPanelSlide : $slideOffset")
                view.setImageSize(slideOffset)
                view.setControllerAlpha(slideOffset)
            }

            override fun onPanelStateChanged(
                panel: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                Log.e("PanelState","$previousState -> $newState")
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
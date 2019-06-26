package com.example.allmysound.Music

import com.example.allmysound.Base.BaseContract

interface MusicFragContract {
    interface View : BaseContract.View{
        override fun showToast(message: String)
    }

    interface Presenter:BaseContract.Presenter<View>{
        override fun setView(view: View)

        override fun releaseView()
    }
}
package com.example.allmysound.Base

interface BaseContract {
    interface View{

    }
    interface Presenter<T>{
        fun setView(view: T)

        fun releaseView()
    }
}
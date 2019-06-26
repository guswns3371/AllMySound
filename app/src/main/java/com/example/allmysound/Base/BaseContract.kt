package com.example.allmysound.Base

interface BaseContract {
    interface View{
        fun showToast(message:String)
    }
    public interface Presenter<T>{
        fun setView(view: T)

        fun releaseView()
    }
}
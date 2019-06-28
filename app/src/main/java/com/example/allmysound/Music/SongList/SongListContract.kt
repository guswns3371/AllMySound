package com.example.allmysound.Music.SongList

import com.example.allmysound.Base.BaseContract
import com.example.allmysound.SongInfo

interface SongListContract {

    interface View:BaseContract.View{
        /**View : 실질적으로 화면에 보여주는 부분(Activity,Fragment,View)*/

    }
    interface Presenter: BaseContract.Presenter<View>{
        /**Presenter : 전달자로서 VIew에 표시될 데이터를 Model에서 가져와 VIew에  전달*/

        override fun setView(view: View)

        override fun releaseView()

        fun loadSong():ArrayList<SongInfo>

    }
}
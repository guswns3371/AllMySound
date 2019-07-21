package com.example.allmysound.Base.BasicListFrag

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allmysound.Main.MainActivity
import com.example.allmysound.Main.Model.SongInfo
import com.example.allmysound.R

class BasicListFrag(
    private var datalist : ArrayList<SongInfo>
) :Fragment(){

    val DATA_RECEIVE = "ToolbarTitle"
    private lateinit var mRecyclerView : androidx.recyclerview.widget.RecyclerView
    private lateinit var linearLayoutManager : LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_basic,container,false)

        mRecyclerView = view.findViewById(R.id.basic_RV)

        if(activity is AppCompatActivity)
            (activity as AppCompatActivity).supportActionBar?.title = getFragArgs()
            // 대박 이전에 만든 툴바를 사용할 수 있다!

        linearLayoutManager = LinearLayoutManager(activity!!)
        initRecyclerView()
        return view
    }

    override fun onDestroyView() {
        if(activity is AppCompatActivity)
            (activity as AppCompatActivity).supportActionBar?.title = ""
        // 대박 이전에 만든 툴바를 사용할 수 있다!
        super.onDestroyView()
    }

    private fun initRecyclerView(){
        val myAdapter = BasicListAdapter(activity!!, datalist)
        myAdapter.mClickListener = object : BasicListAdapter.BasicListClickListener{
            override fun onClick(pos: Int) {
                MainActivity.createMainPresenter().LinkDataList(datalist)
                MainActivity.createMainPresenter().SonglinkDataIndex(pos)
                MainActivity.createMainPresenter().checkIsPlaying()
                MainActivity.createMainPresenter().setRandomIdxNumList()
            }
        }
        MainActivity.createMainPresenter().LinkAdapter(myAdapter)
        mRecyclerView.adapter = myAdapter
        mRecyclerView.layoutManager=linearLayoutManager
        mRecyclerView.setHasFixedSize(true)
    }
     fun getFragArgs(): String =arguments!!.getString(DATA_RECEIVE)!!
}
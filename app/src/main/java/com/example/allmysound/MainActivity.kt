package com.example.allmysound
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.allmysound.Music.MusicFrag
import com.example.allmysound.Recommend.RecommendFrag
import com.example.allmysound.Search.SearchFrag
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.music_control.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(),MainContract.View{

    private  var presenter: MainPresenter= MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar(main_toolbar)
        connectFragments(BtmNavView)
        presenter.setView(this)
        presenter.slidingPanelLayoutListener(sliding_layout)
    }

    override fun showToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
    override fun connectFragments(view : BottomNavigationView){
        view.setOnNavigationItemSelectedListener {
            var selectedFrag : androidx.fragment.app.Fragment? =null
            when(it.itemId){
                R.id.menu_Music -> selectedFrag = MusicFrag()
                R.id.menu_Recommended -> selectedFrag = RecommendFrag()
                R.id.menu_Search -> selectedFrag = SearchFrag()
            }
            supportFragmentManager.beginTransaction().replace(R.id.frag_container,selectedFrag!!).commit()
            true
        }
        supportFragmentManager.beginTransaction().replace(R.id.frag_container, MusicFrag()).commit()
    }
    override fun setToolbar(toolbar: Toolbar){
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar!!.title = ""
        actionbar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.menu_Music -> {
                supportFragmentManager.beginTransaction().replace(R.id.frag_container,MusicFrag()).commit()
                return true
            }
            R.id.menu_Recommended -> {
                supportFragmentManager.beginTransaction().replace(R.id.frag_container,RecommendFrag()).commit()
                return true
            }
            R.id.menu_Search -> {
                supportFragmentManager.beginTransaction().replace(R.id.frag_container,SearchFrag()).commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setBNVHeight(distance:Float){
        val originalHeight = 196
        val height= (originalHeight.toFloat())-( (originalHeight.toFloat())*((10000.0f-distance*10000.0f)/10000.0f) )
        Log.e("setBNVHeight","setBNVHeight : $height")
        BtmNavView.animate()
            .translationY(height)
            .duration=0
    }
    override fun setBNVSize(distance:Float){
        val originalHeight = 196
        BtmNavView.requestLayout()
        BtmNavView.layoutParams.height =  ( (originalHeight.toFloat())*((10000.0f-distance*10000.0f)/10000.0f) ).toInt()
        Log.e("setBNVSize","setBNVSize : ${BtmNavView.height}")
    }
    override fun setControllerAlpha(distance:Float){
        val alphaValue = (10000.0f-distance*40000.0f)
        music_control_two.animate()
            .alpha(alphaValue/10000.0f)
            .duration = 0
    }
    override fun setImageSize(distance:Float){
        val originalHeight = 228
        val marginParams = ctl_cover.layoutParams as MarginLayoutParams
        ctl_cover.requestLayout()
        ctl_cover.layoutParams.height = ((distance*800.0f).toInt()+originalHeight)
        ctl_cover.layoutParams.width = ((distance*800.0f).toInt()+originalHeight)
        marginParams.setMargins(
            (distance*200.0f).toInt(),(distance*220.0f).toInt(),
            (distance*220.0f).toInt(),(distance*220.0f).toInt()  )
    }
}

package com.example.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.adapterdata.Data
import com.example.githubuser.adapterdata.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_list_view.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        getdata()
        val pageradapter = PagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.pager)
        viewPager.adapter = pageradapter
        val tabs: TabLayout = findViewById(R.id.tabs_follow)
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }
    companion object{
        const val EXTRA_MYDATA = "extra_mydata"
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun getdata(){
        val datausers = intent.getParcelableExtra(EXTRA_MYDATA) as Data
        datausers.name?.let { actionbartittle(it) }
        detailname.text = datausers.name
        username_detail.text = datausers.username
        repository_.text = datausers.repository
        following_.text = datausers.following
        followers_.text = datausers.followers
        company_detail.text = datausers.company
        location_detail.text = datausers.location
        blog_detail.text = datausers.blog
        Glide.with(this).load(datausers.avatar).into(circleImageView)
    }
    fun actionbartittle(string: String) {
        if (supportActionBar != null){
            this.title = string
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language){
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
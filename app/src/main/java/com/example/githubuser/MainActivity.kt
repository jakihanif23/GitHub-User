package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapterdata.Data
import com.example.githubuser.adapterdata.ListViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val list =ArrayList<Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_list.setHasFixedSize(true)
        list.addAll(getListMyDatas())
        showRecyclerlist()
    }

    private fun getListMyDatas() : ArrayList<Data>{
        val dataUsername = resources.getStringArray(R.array.username)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val dataName = resources.getStringArray(R.array.name)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataFollowers = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)
        val listMyData = ArrayList<Data>()
        for (position in dataName.indices){
            val keyData = Data(
                dataUsername[position],
                dataLocation[position],
                dataCompany[position],
                dataAvatar.getResourceId(position,-2),
                dataName[position],
                dataRepository[position],
                dataFollowers[position],
                dataFollowing[position]
            )
            listMyData.add(keyData)
        }
        dataAvatar.recycle()
        return listMyData
    }
    private fun showRecyclerlist(){
        rv_list.layoutManager = LinearLayoutManager(this)
        val listAdapter = ListViewAdapter(this@MainActivity, list)
        rv_list.adapter = listAdapter
    }
}
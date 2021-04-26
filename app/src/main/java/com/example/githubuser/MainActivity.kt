package com.example.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.loopj.android.http.*;
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.Token.GitHubToken
import com.example.githubuser.`interface`.ItemCallBack
import com.example.githubuser.adapterdata.Data
import com.example.githubuser.adapterdata.ListViewAdapter
import com.example.githubuser.adapterdata.lists
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var list: ArrayList<Data> = ArrayList()
    private lateinit var adapter: ListViewAdapter
    private var base_url = "https://api.github.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = resources.getString(R.string.app_name)
        adapter = ListViewAdapter(list)
        rv()
        getusers()
    }
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private fun rv() {
        rv_list.layoutManager = LinearLayoutManager(rv_list.context)
        rv_list.setHasFixedSize(true)
        rv_list.addItemDecoration(
            DividerItemDecoration(
                rv_list.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
    private fun recyclerlist(){
        rv_list.layoutManager = LinearLayoutManager(this)
        val listViewAdapter = ListViewAdapter(lists)
        rv_list.adapter = adapter
        listViewAdapter.setItemCallBack(object : ItemCallBack{
            override fun onItemClicked(dataUsers: Data) {
                showselectusers(dataUsers)
            }
        })
    }
    fun showselectusers(data: Data){
        Data(
            data.username,
            data.company,
            data.location,
            data.avatar,
            data.name,
            data.repository,
            data.followers,
            data.following,
            data.blog
        )
        val intent = Intent(this,DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MYDATA,data)
        this.startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query!!.isEmpty()){
                    return true
                }else{
                    list.clear()
                    searchuser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        } )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language){
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return super.onOptionsItemSelected(item)
    }
    fun getdetail(string: String){
        progressBarMain.visibility = View.VISIBLE
        AsyncHttpClient().apply { addHeader("User-Agent","request") }.apply { addHeader("Authorization",GitHubToken.TOKEN_GITHUB_KEY) }
            .get(base_url + "users/$string", object : AsyncHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    progressBarMain.visibility = View.INVISIBLE
                    val hasil = String(responseBody!!)
                    Log.d(TAG, hasil)
                    try {
                        val jsonObject = JSONObject(hasil)
                        val username: String = jsonObject.getString("login").toString()
                        val name: String = jsonObject.getString("name").toString()
                        val avatar: String = jsonObject.getString("avatar_url").toString()
                        val company: String = jsonObject.getString("company").toString()
                        val location: String = jsonObject.getString("location").toString()
                        val repository: String? = jsonObject.getString("public_repos")
                        val followers: String? = jsonObject.getString("followers")
                        val following: String? = jsonObject.getString("following")
                        val blog: String? = jsonObject.getString("blog")
                        list.add(
                            Data(
                                username,
                                company,
                                location,
                                avatar,
                                name,
                                repository,
                                followers,
                                following,
                                blog
                            )
                        )
                        recyclerlist()
                    }catch (e: Exception){
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(this@MainActivity, error?.message.toString(),Toast.LENGTH_LONG).show()
                }

            })
    }
    fun searchuser(string: String){
        progressBarMain.visibility = View.VISIBLE
        AsyncHttpClient().apply { addHeader("User-Agent","request") }.apply { addHeader("Authorization",GitHubToken.TOKEN_GITHUB_KEY) }
            .get(base_url + "search/users?q=$string", object : AsyncHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    progressBarMain.visibility = View.INVISIBLE
                    val hasil = String(responseBody!!)
                    Log.d(TAG, hasil)
                    try {
                        val jsonArray = JSONObject(hasil)
                        val list =jsonArray.getJSONArray("items")
                        for (i in 0 until list.length()){
                            val jsonObject = list.getJSONObject(i)
                            val username: String = jsonObject.getString("login")
                            getdetail(username)
                        }
                    } catch (e: Exception){
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(this@MainActivity, error?.message.toString(),Toast.LENGTH_LONG).show()
                }

            })
    }
    fun getusers(){
        progressBarMain.visibility = View.VISIBLE
        AsyncHttpClient().apply { addHeader("User-Agent","request") }.apply { addHeader("Authorization",GitHubToken.TOKEN_GITHUB_KEY) }
            .get(base_url + "users",object : AsyncHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>,
                    responseBody: ByteArray
                ) {
                    progressBarMain.visibility = View.INVISIBLE
                    val hasil = String(responseBody)
                    Log.d(TAG,hasil)
                    try {
                        val jsonArray = JSONArray(hasil)
                        for (i in 0 until jsonArray.length()){
                            val jsonObject = jsonArray.getJSONObject(i)
                            val username: String = jsonObject.getString("login")
                            getdetail(username)
                        }
                    } catch (e: Exception){
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(this@MainActivity, error?.message.toString(),Toast.LENGTH_LONG).show()
                }

            })
    }
}
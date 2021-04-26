package com.example.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.DetailActivity
import com.example.githubuser.R
import com.example.githubuser.Token.GitHubToken
import com.example.githubuser.`interface`.ItemCallBack
import com.example.githubuser.adapterdata.Data
import com.example.githubuser.adapterdata.FollowersAdapter
import com.example.githubuser.adapterdata.listsfollowers
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FragmentFollowers : Fragment() {

    private var base_url = "https://api.github.com/"
    private var lisfollowers: ArrayList<Data> = ArrayList()
    private lateinit var followersAdapter: FollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followersAdapter = FollowersAdapter(lisfollowers)
        lisfollowers.clear()
        getfollowers((activity!!.intent.getParcelableExtra(EXTRA_DATA) as Data).username.toString())
    }
    companion object {
        private val TAG = FragmentFollowers::class.java.simpleName
        const val EXTRA_DATA = "extra_data"
    }
    private fun recyclerlist(){
        rv_list_followers.layoutManager = LinearLayoutManager(activity)
        rv_list_followers.adapter = followersAdapter
        FollowersAdapter(listsfollowers).setcallback(object : ItemCallBack{
            override fun onItemClicked(dataUsers: Data) {

            }
        })
    }
    fun getdetailfollowers(string: String){
        progressBarMain.visibility = View.VISIBLE
        AsyncHttpClient().apply { addHeader("User-Agent","request") }.apply { addHeader("Authorization",
            GitHubToken.TOKEN_GITHUB_KEY) }
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
                        lisfollowers.add(
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
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(activity, error?.message.toString(), Toast.LENGTH_LONG).show()
                }

            })
    }
    fun getfollowers(string: String){
        progressBarMain.visibility = View.VISIBLE
        AsyncHttpClient().apply { addHeader("User-Agent","request") }.apply { addHeader("Authorization",GitHubToken.TOKEN_GITHUB_KEY) }
            .get(base_url + "users/$string/followers",object : AsyncHttpResponseHandler(){
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
                            getdetailfollowers(username)
                        }
                    } catch (e: Exception){
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    Toast.makeText(activity, error?.message.toString(),Toast.LENGTH_LONG).show()
                }

            })
    }
}
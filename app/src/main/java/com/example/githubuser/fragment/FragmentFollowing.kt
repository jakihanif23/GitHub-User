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
import com.example.githubuser.adapterdata.FollowingAdapter
import com.example.githubuser.adapterdata.listsfollowing
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FragmentFollowing : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        followingAdapter = FollowingAdapter(lisfollowing)
        lisfollowing.clear()
        val get = activity!!.intent.getParcelableExtra(EXTRA_MYDATA) as Data
        getfollowing(get.username.toString())
        super.onViewCreated(view, savedInstanceState)
    }
    companion object{
        private val TAG = FollowingAdapter::class.java.simpleName
        const val EXTRA_MYDATA = "extra_data"
    }
    private var lisfollowing: ArrayList<Data> = ArrayList()
    private lateinit var followingAdapter: FollowingAdapter
    private var base_url = "https://api.github.com/"
    private fun recyclerlist(){
        rv_list_following.layoutManager = LinearLayoutManager(activity)
        rv_list_following.adapter = followingAdapter
        FollowingAdapter(listsfollowing).setcallback(object : ItemCallBack{
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
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MYDATA,data)
        this.startActivity(intent)
    }
    fun getdetailfollowing(string: String){
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
                        lisfollowing.add(
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
    fun getfollowing(string: String){
        progressBarMain.visibility = View.VISIBLE
        AsyncHttpClient().apply { addHeader("User-Agent","request") }.apply { addHeader("Authorization",
            GitHubToken.TOKEN_GITHUB_KEY) }
            .get(base_url + "users/$string/following",object : AsyncHttpResponseHandler(){
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
                            getdetailfollowing(username)
                        }
                    } catch (e: Exception){
                        Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
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
                    Toast.makeText(activity, error?.message.toString(), Toast.LENGTH_LONG).show()
                }

            })
    }
}
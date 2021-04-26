package com.example.githubuser.adapterdata

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.DetailActivity
import com.example.githubuser.R
import com.example.githubuser.`interface`.ItemCallBack
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_list_view.view.*

var listsfollowers = ArrayList<Data>()
class FollowersAdapter(list: ArrayList<Data>): RecyclerView.Adapter<FollowersAdapter.DataHolder>(){
    class DataHolder (view: View): RecyclerView.ViewHolder(view){
        var img: CircleImageView = view.imagelist
        var fullname: TextView = view.name_list
        var username: TextView = view.username_list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_view,parent,false)
        context = parent.context
        return DataHolder(view)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        val a = listsfollowers[position]
        holder.fullname.text = a.name
        holder.username.text = a.username
        Glide.with(holder.itemView.context).load(a.avatar).into(holder.img)
        holder.itemView.setOnClickListener{
            val b = Data(
                a.username,
                a.company,
                a.location,
                a.avatar,
                a.name,
                a.repository,
                a.followers,
                a.following,
                a.blog
            )
            context.startActivity(Intent(context,DetailActivity::class.java).putExtra(DetailActivity.EXTRA_MYDATA, b))
        }
    }

    override fun getItemCount(): Int = listsfollowers.size
    init {
        listsfollowers = list
    }
    private var itemCallBack: ItemCallBack? = null
    fun setcallback(i: ItemCallBack){
        this.itemCallBack = i
    }
}
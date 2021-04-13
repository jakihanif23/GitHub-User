package com.example.githubuser.adapterdata

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuser.DetailActivity
import com.example.githubuser.R
import kotlinx.android.synthetic.main.item_list_view.view.*

class ListViewAdapter (private val context: Context, private val items: ArrayList<Data>) : RecyclerView.Adapter<ListViewAdapter.DataViewHolder>(){
    inner class DataViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(data: Data){
            with(itemView){
                Glide.with(itemView.context)
                    .load(data.avatar)
                    .apply(RequestOptions().override(70,70))
                    .into(imagelist)
                name_list.text = data.name
                followers_number_list.text = data.followers
                following_number_list.text = data.following
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_view, parent, false)
        return DataViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val keyData = items[position]
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_MYDATA, keyData)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

}
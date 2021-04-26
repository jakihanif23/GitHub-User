package com.example.githubuser.adapterdata

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.DetailActivity
import com.example.githubuser.R
import com.example.githubuser.`interface`.ItemCallBack
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_list_view.view.*
import java.util.*
import kotlin.collections.ArrayList


var lists = ArrayList<Data>()
@SuppressLint("StaticFieldLeak")
lateinit var context: Context
class ListViewAdapter(private var arraydata: ArrayList<Data>): RecyclerView.Adapter<ListViewAdapter.DataHolder>(), Filterable{
    inner class DataHolder (view: View): RecyclerView.ViewHolder(view){
        var image: CircleImageView = itemView.imagelist
        var name_lists: TextView = itemView.name_list
        var username_lists: TextView = itemView.username_list
    }
    init {
        lists = arraydata
    }
    private var itemCallback: ItemCallBack? = null
    fun setItemCallBack(itemCallBack: ItemCallBack){
        this.itemCallback = itemCallback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_view,parent,false)
        context = parent.context
        return DataHolder(view)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        val userlists = lists[position]
        Glide.with(holder.itemView.context)
            .load(userlists.avatar)
            .into(holder.image)
        holder.name_lists.text = userlists.name
        holder.username_lists.text = userlists.username
        holder.itemView.setOnClickListener{
            val datalists = Data(
                userlists.username,
                userlists.company,
                userlists.location,
                userlists.avatar,
                userlists.name,
                userlists.repository,
                userlists.followers,
                userlists.following,
                userlists.blog
            )
            val intentdetail = Intent(context, DetailActivity::class.java)
            intentdetail.putExtra(DetailActivity.EXTRA_MYDATA, datalists)
            context.startActivity(intentdetail)
        }
    }

    override fun getItemCount(): Int = lists.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val cari = constraint.toString()
                lists = if (cari.isEmpty()) {
                    arraydata
                } else {
                    val hasilcari = ArrayList<Data>()
                    for (row in lists) {
                        if ((row.username.toString().toLowerCase(Locale.ROOT)
                                .contains(cari.toLowerCase(Locale.ROOT)))
                        ) {
                            hasilcari.add(
                                Data(
                                    row.username,
                                    row.company,
                                    row.location,
                                    row.avatar,
                                    row.name,
                                    row.repository,
                                    row.followers,
                                    row.following,
                                    row.blog
                                )
                            )
                        }
                    }
                    hasilcari
                }
                val hasilfilter = FilterResults()
                hasilfilter.values = lists
                return hasilfilter
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                lists = results.values as ArrayList<Data>
                notifyDataSetChanged()
            }
        }
    }
}
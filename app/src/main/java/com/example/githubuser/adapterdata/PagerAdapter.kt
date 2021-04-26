package com.example.githubuser.adapterdata

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.R
import com.example.githubuser.fragment.FragmentFollowers
import com.example.githubuser.fragment.FragmentFollowing

class PagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var fragment: Fragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> fragment = FragmentFollowers()
            1 -> fragment = FragmentFollowing()
        }
        return fragment as Fragment
    }
}
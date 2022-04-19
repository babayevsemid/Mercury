package com.example.storymodule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val list: ArrayList<Fragment> = arrayListOf()

    override fun getItemCount()=list.size

    override fun createFragment(position: Int)= list[position]

    fun add(fragment: Fragment){
        list.add(fragment)
    }
}
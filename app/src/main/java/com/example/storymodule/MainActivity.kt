package com.example.storymodule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storymodule.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = PagerAdapter(supportFragmentManager, lifecycle)
        adapter.add(TabFragment())
        adapter.add(TabFragment())

        binding.viewPager.adapter = adapter
    }

    fun getViewPager2() = binding.viewPager
}
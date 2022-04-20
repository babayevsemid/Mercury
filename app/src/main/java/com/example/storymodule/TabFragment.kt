package com.example.storymodule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.samid.story.data.model.StoryModel
import com.example.storymodule.databinding.FragmentTabBinding


class TabFragment : Fragment() {
    private val binding by lazy {
        FragmentTabBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        init()
        return binding.root
    }

    private fun init() {
        val activity = activity as MainActivity
        val viewPager2 = activity.getViewPager2()

        binding.storyView.setupViewPager2(viewPager2)
        binding.storyView.setLifeCycle(lifecycle)
        binding.storyView.setStoryList(getStoryList())
        binding.storyView.onSwipe = {
            Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
        }

        binding.storyView.showPrevStories = {
            if (viewPager2.currentItem > 0)
                viewPager2.currentItem = viewPager2.currentItem - 1
        }
        binding.storyView.showNextStories = {
            if (viewPager2.currentItem < viewPager2.childCount)
                viewPager2.currentItem = viewPager2.currentItem + 1
        }
    }

    private fun getStoryList() = arrayListOf(
        StoryModel(
            profileName = "Title",
            profileImageVisible = false,
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSD0XLEaT5CtjF-wjpktGBOGZ3zcNZuyVVj5osLcVXyra9HytvgxyiLclfJpQDDcZQbaQ&usqp=CAU",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
        ),
        StoryModel(
            profileName = "Title1",
            profileImage = "https://avatarfiles.alphacoders.com/184/thumb-184230.jpg",
            photoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/1200px-Image_created_with_a_mobile_phone.png",
        ),
        StoryModel(
            profileName = "Title2",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT45K2gpY4-Uoaj5QdFuPPx4Ua8UZ2Q9KuKVXJ7Ef8mFPgxZ0OfR77IwfIoimihYxb0eeU&usqp=CAU",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        ),
        StoryModel(
            profileName = "Title3",
            profileImage = "https://qph.fs.quoracdn.net/main-thumb-1278318002-200-ydzfegagslcexelzgsnplcklfkienzfr.jpeg",
            photoUrl = "https://wallpapercave.com/wp/wp3307160.jpg"
        ),
        StoryModel(
            profileName = "Title4",
            profileImage = "https://qph.fs.quoracdn.net/main-thumb-221437051-200-uwybhhljjxyshwoiabsctechmxfjpjoi.jpeg",
            photoUrl = "https://cutewallpaper.org/21/1080x1920-4k-wallpaper/1080x1920-4k-Wallpaper-Nature-Fitrinis-Wallpaper.jpg"
        ),
        StoryModel(
            profileName = "Title5",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn5YoK2qKTpqEz-rX_d6Khmn-4sP-GPGzw-Q&usqp=CAU",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4"
        ),
        StoryModel(
            profileName = "Title6",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ9HLFktTeyuLKSmhRnRcLqIL8arOeynElFhA&usqp=CAU",
            photoUrl = "https://i.pinimg.com/564x/fe/32/18/fe32187635f4f05d2963fc570e38a680.jpg"
        )
    )
}
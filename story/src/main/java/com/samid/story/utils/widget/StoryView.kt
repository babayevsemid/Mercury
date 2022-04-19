package com.samid.story.utils.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.exoplayer2.*
import com.samid.story.R
import com.samid.story.data.model.StoryModel
import com.samid.story.databinding.WidgetStoryViewBinding
import com.samid.story.utils.OnSwipeListener
import com.samid.story.utils.Utils
import com.samid.story.utils.getStatusBarHeight
import com.samid.story.utils.setPaddingTop


class StoryView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs),
    LifecycleEventObserver, View.OnTouchListener {

    private val binding by lazy {
        WidgetStoryViewBinding.inflate(LayoutInflater.from(context), this)
    }
    private var storyList: List<StoryModel>? = null
    private var gestureDetector: GestureDetector? = null
    private var exoPlayer: ExoPlayer? = null
    private var viewPager2: ViewPager2? = null
    private var isResumed: Boolean = false

    var onStoryChanged: (model: StoryModel) -> Unit = {}

    var showNextStories: () -> Unit = {}
    var showPrevStories: () -> Unit = {}
    var onSwipe: (direction: OnSwipeListener.Direction) -> Unit = {}
    var onCloseClick: () -> Unit = {}

    init {
        gestureDetector = GestureDetector(context, object : OnSwipeListener() {
            override fun onSwipe(direction: Direction?): Boolean {
                this@StoryView.onSwipe(direction)
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                binding.headerView.isVisible = false
                viewPager2?.isUserInputEnabled = false
            }
        })

        context.obtainStyledAttributes(attrs, R.styleable.StoryView).apply {
            val fitsSystemWindow = getBoolean(R.styleable.StoryView_fitsSystemWindows, false)
            if (fitsSystemWindow)
                binding.headerView.setPaddingTop(context.getStatusBarHeight())
            recycle()
        }

        initStoryChange()
        setClicks()
    }

    private fun initStoryChange() {
        binding.headerView.onStoryChanged = { model ->
            exoPlayer?.stop()
            exoPlayer = null

            onStoryChanged.invoke(model)

            binding.coverImg.isVisible = !model.isVideo
            binding.playerView.isVisible = model.isVideo

            showLoading()

            if (model.isVideo) {
                loadVideo(model)
            } else
                loadPhoto(model)

            binding.headerView.setProfileImageVisible(model.profileImageVisible)
            binding.seeMoreGroup.isVisible = model.seeMoreVisible
        }
    }

    private fun loadPhoto(model: StoryModel) {
        Glide.with(context.applicationContext)
            .load(model.photoUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    showError()
                    binding.headerView.pause()

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    showContent()

                    if (isResumed)
                        binding.headerView.resume()

                    return false
                }
            })
            .into(binding.coverImg)
    }

    private fun loadVideo(model: StoryModel) {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                this.exoPlayer = exoPlayer
                binding.playerView.player = exoPlayer

                exoPlayer.addListener(playbackStateListener)
                exoPlayer.addMediaItem(MediaItem.fromUri(Uri.parse(model.videoUrl)))
                exoPlayer.playWhenReady = true
                exoPlayer.prepare()
            }
    }

    private val playbackStateListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)

            binding.headerView.pause()
            showError()
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            exoPlayer?.let {
                when (playbackState) {
                    ExoPlayer.STATE_READY -> {
                        binding.headerView.setProgressMaxDuration(it.duration.toInt())

                        if (isResumed) {
                            if (exoPlayer?.isPlaying == true)
                                binding.headerView.resume()
                            else
                                binding.headerView.pause()
                        } else {
                            it.pause()
                        }

                        showContent()
                    }
                    ExoPlayer.STATE_BUFFERING ->
                        showLoading()
                    ExoPlayer.STATE_ENDED ->
                        binding.headerView.next()
                    else -> {
                    }
                }
            }
        }
    }

    private fun setClicks() {
        binding.headerView.showNextStories = {
            showNextStories.invoke()
        }

        binding.headerView.showPrevStories = {
            showPrevStories.invoke()
        }

        binding.headerView.onCloseClick = {
            onCloseClick.invoke()
        }

        binding.root.setOnTouchListener(this)
    }

    fun setStoryList(storyList: List<StoryModel>?) {
        this.storyList = storyList

        binding.headerView.setStoryList(storyList)
    }

    fun setLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    fun setupViewPager2(viewPager2: ViewPager2) {
        this.viewPager2 = viewPager2

        var currentItem = viewPager2.currentItem

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (currentItem != position) {
                    exoPlayer?.pause()
                    exoPlayer?.seekTo(0)

                    binding.headerView.reset()

                    currentItem = position
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == 0 && currentItem == viewPager2.currentItem) {
                    onResume()
                }
            }
        })
    }

    fun onResume() {
        if (isResumed) {
            if (binding.coverImg.isVisible && binding.coverImg.drawable != null)
                binding.headerView.resume()

            exoPlayer?.play()
        }
    }

    fun onPause() {
        exoPlayer?.pause()

        binding.headerView.pause()
    }

    private fun onSwipe(direction: OnSwipeListener.Direction?) {
        direction?.let {
            onSwipe.invoke(it)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        gestureDetector?.onTouchEvent(event)

        if (Utils.onClickScreenLeft(event)) {
            binding.headerView.prev()
        }

        if (Utils.onClickScreenRight(event)) {
            binding.headerView.next()
        }

        if (Utils.actionDown(event)) {
            binding.headerView.pause()

            exoPlayer?.pause()
        }

        if (Utils.actionUp(event)) {
            if (binding.coverImg.isVisible && binding.coverImg.drawable != null)
                binding.headerView.resume()

            exoPlayer?.play()

            binding.headerView.isVisible = true
            viewPager2?.isUserInputEnabled = true
        }
        return true
    }

    private fun showError() {
        binding.errorGroup.isVisible = true
        binding.progressIndicator.isVisible = false
    }

    private fun showLoading() {
        binding.errorGroup.isVisible = false
        binding.progressIndicator.isVisible = true
    }

    private fun showContent() {
        binding.errorGroup.isVisible = false
        binding.progressIndicator.isVisible = false
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                isResumed = true
                onResume()
            }
            Lifecycle.Event.ON_PAUSE -> {
                isResumed = false
                onPause()
            }
            else -> {

            }
        }
    }
}
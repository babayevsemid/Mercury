package com.samid.story.utils.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.isVisible
import com.samid.story.R
import com.samid.story.data.model.StoryModel
import com.samid.story.databinding.StoryViewHeaderBinding
import com.samid.story.databinding.StoryViewProgressBinding
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.LinearProgressIndicator

class StoryViewHeader(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private val binding by lazy {
        StoryViewHeaderBinding.inflate(LayoutInflater.from(context), this)
    }
    private var storyList: List<StoryModel>? = null
    private var objectAnimator: ObjectAnimator? = null
    private var currentProgressIndicator: LinearProgressIndicator? = null
    private var currentIndex = -1

    var onStoryChanged: (model: StoryModel) -> Unit = {}

    var showNextStories: () -> Unit = {}
    var showPrevStories: () -> Unit = {}
    var onCloseClick: () -> Unit = {}

    init {
        setBackgroundResource(R.drawable.bg_story_view_gradient_top)

        binding.closeBtn.setOnClickListener { onCloseClick.invoke() }
    }

    fun setStoryList(storyList: List<StoryModel>?) {
        this.storyList = storyList

        binding.progressContainer.removeAllViews()

        storyList?.forEachIndexed { index, model ->
            createProgress(model)

            if (model.completed)
                fillProgressIndicator(index)
            else if (currentIndex == -1)
                currentIndex = index
        }

        if (currentIndex < -1)
            currentIndex = 0

        changeCurrentProgressIndicator(currentIndex)
    }

    private fun createProgress(model: StoryModel) {
        StoryViewProgressBinding.inflate(
            LayoutInflater.from(context),
            binding.progressContainer, true
        ).root.apply {
            max = model.duration.toInt()
        }
    }

    fun next() {
        val nextIndex = currentIndex + 1

        if (nextIndex >= binding.progressContainer.childCount) {
            pause()

            showNextStories.invoke()
        } else
            changeCurrentProgressIndicator(index = nextIndex)
    }

    fun prev() {
        val prevIndex = currentIndex - 1

        if (prevIndex < 0) {
            pause()

            clearProgressIndicator(currentIndex)

            showPrevStories.invoke()
        } else
            changeCurrentProgressIndicator(index = prevIndex)
    }

    fun resume() {
        if (objectAnimator?.isStarted == true)
            objectAnimator?.resume()
        else
            objectAnimator?.start()
    }

    fun pause() {
        objectAnimator?.pause()
    }

    fun reset() {
        objectAnimator?.pause()
        objectAnimator = null

        currentProgressIndicator = getProgressIndicator(currentIndex)
        currentProgressIndicator?.setSmoothProgress()

        clearProgressIndicator(currentIndex)
    }

    private fun changeCurrentProgressIndicator(index: Int) {
        objectAnimator?.pause()
        objectAnimator = null

        if (index > currentIndex) {
            fillProgressIndicator(currentIndex)
        } else if (index < currentIndex) {
            clearProgressIndicator(index = currentIndex)
            clearProgressIndicator(index = index)
        }

        currentProgressIndicator = getProgressIndicator(index)
        currentProgressIndicator?.setSmoothProgress()

        storyList?.let {
            onStoryChanged.invoke(it[index])
        }

        loadModel(index)
        currentIndex = index
    }

    private fun loadModel(index: Int) {
        storyList?.get(index)?.let {
            binding.titleTxt.text = it.profileName
            binding.avatarImg.isVisible = it.profileImageVisible

            Glide.with(context.applicationContext).load(it.profileImage).into(binding.avatarImg)
        }
    }

    private fun fillProgressIndicator(index: Int) {
        getProgressIndicator(index).apply {
            progress = max
        }
    }

    private fun clearProgressIndicator(index: Int) {
        getProgressIndicator(index).apply {
            progress = 0
        }
    }

    fun setProgressMaxDuration(maxDuration: Int) {
        if (currentProgressIndicator?.max != maxDuration) {
            objectAnimator?.pause()
            objectAnimator = null

            currentProgressIndicator = getProgressIndicator(currentIndex).apply {
                max = maxDuration
            }
            currentProgressIndicator?.setSmoothProgress()
        }
    }

    private fun getProgressIndicator(index: Int) =
        binding.progressContainer[index] as LinearProgressIndicator

    private fun LinearProgressIndicator.setSmoothProgress() {
        objectAnimator = ObjectAnimator.ofInt(this, "progress", max)
        objectAnimator?.duration = max.toLong()
        objectAnimator?.interpolator = LinearInterpolator()
        objectAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                next()
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    fun setProfileImageVisible(isVisible: Boolean) {
        binding.avatarImg.isVisible = isVisible
    }
}
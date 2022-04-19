package com.samid.story.data.model

data class StoryModel(
    val profileName: String?,
    val profileImage: String?,
    val photoUrl: String? = null,
    val videoUrl: String? = null,
    val duration: Long = 5000,
    val completed: Boolean = false,
    val profileImageVisible: Boolean = true,
    val seeMoreVisible: Boolean = true
){
    val isVideo get() = videoUrl.isNullOrEmpty().not()
}
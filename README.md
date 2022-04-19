# Mercury

### Installation

Add this to your ```build.gradle``` file

```
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    implementation 'com.github.babayevsemid:Mercury:1.0.0"
}
```
### Use

```
 storyView.setupViewPager2(viewPager2)
 storyView.setLifeCycle(lifecycle)
 storyView.setStoryList(getStoryList())
 
 storyView.onSwipe = {
     Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
 }

 storyView.showPrevStories = {
   if (viewPager2.currentItem > 0)
       viewPager2.currentItem = viewPager2.currentItem - 1
 }
 
 storyView.showNextStories = {
            if (viewPager2.currentItem < viewPager2.childCount)
                viewPager2.currentItem = viewPager2.currentItem + 1
        }
        
        
private fun getStoryList() = arrayListOf(
        StoryModel(
            profileName = "Title",
            profileImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSD0XLEaT5CtjF-wjpktGBOGZ3zcNZuyVVj5osLcVXyra9HytvgxyiLclfJpQDDcZQbaQ&usqp=CAU",
            photoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/1200px-Image_created_with_a_mobile_phone.png",
        )
}
``` 
  
### StoryModel

``` 
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
        
``` 

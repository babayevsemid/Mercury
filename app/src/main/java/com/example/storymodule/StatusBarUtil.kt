package com.example.storymodule

import android.app.Activity
import android.graphics.Color
import android.view.View


object StatusBarUtil {
    fun setTranslucent(activity: Activity) {
        activity.window.statusBarColor = Color.TRANSPARENT

        val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        activity.window.decorView.systemUiVisibility = flags
    }
}



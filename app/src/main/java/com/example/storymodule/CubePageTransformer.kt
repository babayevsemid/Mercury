package com.example.storymodule

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class CubePageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.cameraDistance = page.width * 12f
        page.pivotX = (if (position < 0f) page.width else 0.toFloat()).toFloat()
        page.pivotY = page.height * 0.5f
        page.rotationY = 90f * position
    }
}
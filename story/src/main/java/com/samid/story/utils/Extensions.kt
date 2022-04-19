package com.samid.story.utils

import android.content.Context
import android.view.View

fun Context?.getStatusBarHeight(): Int {
    return this?.let {
        val idStatusBarHeight =
            it.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (idStatusBarHeight > 0) {
            it.resources.getDimensionPixelSize(idStatusBarHeight)
        } else 0
    } ?: 0
}

fun View.setPaddingTop(padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}
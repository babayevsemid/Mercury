package com.samid.story.utils

import android.content.Context
import android.content.res.Resources
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


object Utils {
    private var startX = 0f
    private var startY = 0f
    private var isClick = false

    private val screenWidth by lazy {
        Resources.getSystem().displayMetrics.widthPixels
    }

    fun actionDown(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN)
            return true

        return false
    }

    fun actionUp(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP)
            return true

        return false
    }

    fun onClickScreenLeft(event: MotionEvent): Boolean {
        if (hasCLicked(event)) {
            if (event.rawX < screenWidth * 1 / 2)
                return true
        }
        return false
    }

    fun onClickScreenRight(event: MotionEvent): Boolean {
        if (hasCLicked(event)) {
            if (event.rawX > screenWidth * 1 / 2)
                return true
        }

        return false
    }

    fun hasCLicked(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y

                isClick = true
                timer.start()
            }
            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val endY = event.y

                timer.cancel()
                if (isAClick(startX, endX, startY, endY)) {
                    return isClick
                }
            }
        }
        return false
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val differenceX = abs(startX - endX)
        val differenceY = abs(startY - endY)
        val clickActionHold = 200
        return !(differenceX > clickActionHold /* =5 */ || differenceY > clickActionHold)
    }

    private val timer = object : CountDownTimer(300, 100) {
        override fun onTick(millisUntilFinished: Long) {}

        override fun onFinish() {
            isClick = false
        }
    }
}
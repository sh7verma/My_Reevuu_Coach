package com.myreevuuCoach.utils

import android.view.View
import android.view.ViewTreeObserver

class OnViewGlobalLayoutListener(private val view: View, private val maxHeight: Int) : ViewTreeObserver.OnGlobalLayoutListener {
    override fun onGlobalLayout() {
        if (view.height > maxHeight)
            view.layoutParams.height = maxHeight
    }
}
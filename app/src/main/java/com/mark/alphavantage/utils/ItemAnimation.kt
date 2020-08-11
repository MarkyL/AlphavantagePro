package com.mark.alphavantage.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object ItemAnimation {

    /* animation type */
    const val BOTTOM_UP = 1
    const val FADE_IN = 2
    /* animation duration */
    private const val DURATION_IN_BOTTOM_UP: Long = 250
    private const val DURATION_IN_FADE_IN: Long = 500

    fun animate(view: View, position: Int, type: Int) {
        when (type) {
            BOTTOM_UP -> animateBottomUp(view, position)
            FADE_IN -> animateFadeIn(view, position)
        }
    }

    private fun animateBottomUp(view: View, position: Int) {
        var position = position
        val notFirstItem = position == -1
        position++
        view.translationY = (if (notFirstItem) 800 else 500).toFloat()
        view.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorTranslateY =
            ObjectAnimator.ofFloat(view, "translationY", if (notFirstItem) 800f else 500f, 0f)
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 1f)
        animatorTranslateY.startDelay = if (notFirstItem) 0 else position * DURATION_IN_BOTTOM_UP
        animatorTranslateY.duration = (if (notFirstItem) 3 else 1) * DURATION_IN_BOTTOM_UP
        animatorSet.playTogether(animatorTranslateY, animatorAlpha)
        animatorSet.start()
    }

    private fun animateFadeIn(view: View, position: Int) {
        var position = position
        val notFirstItem = position == -1
        position++
        view.alpha = 0f
        val animatorSet = AnimatorSet()
        val animatorAlpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 0.5f, 1f)
        ObjectAnimator.ofFloat(view, "alpha", 0f).start()
        animatorAlpha.startDelay =
            if (notFirstItem) DURATION_IN_FADE_IN / 2 else position * DURATION_IN_FADE_IN / 3
        animatorAlpha.duration = DURATION_IN_FADE_IN
        animatorSet.play(animatorAlpha)
        animatorSet.start()
    }

}
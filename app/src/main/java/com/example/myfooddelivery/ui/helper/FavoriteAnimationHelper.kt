package com.example.myfooddelivery.ui.helper

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageButton

object FavoriteAnimationHelper {

    private const val SCALE_UP_FROM = 1.0f
    private const val SCALE_UP_TO = 1.5f
    private const val SCALE_DOWN_FROM = 1.5f
    private const val SCALE_DOWN_TO = 1.0f

    fun favoriteChangedAnimation(imageButton: ImageButton) {
        val scaleUpX = ObjectAnimator.ofFloat(imageButton, "scaleX", SCALE_UP_FROM, SCALE_UP_TO)
        val scaleUpY = ObjectAnimator.ofFloat(imageButton, "scaleY", SCALE_UP_FROM, SCALE_UP_TO)
        val scaleDownX = ObjectAnimator.ofFloat(imageButton, "scaleX", SCALE_DOWN_FROM, SCALE_DOWN_TO)
        val scaleDownY = ObjectAnimator.ofFloat(imageButton, "scaleY", SCALE_DOWN_FROM, SCALE_DOWN_TO)
        AnimatorSet().apply {
            val scaleUp = AnimatorSet().apply {
                playTogether(scaleUpX, scaleUpY)
            }
            val scaleDown = AnimatorSet().apply {
                playTogether(scaleDownX, scaleDownY)
            }
            playSequentially(scaleUp, scaleDown)
            start()
        }
    }
}
package com.example.myfooddelivery.ui.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.example.myfooddelivery.databinding.FragmentProfileBinding


object ProfileAnimationHelper {

    private const val DURATION = 200L
    private const val SCALE_X_FROM = 0f
    private const val SCALE_X_TO = 1f
    private const val TRANSLATION_X_FROM = -200f
    private const val TRANSLATION_X_TO = 0f
    private const val TRANSLATION_Y_FROM = 500f
    private const val TRANSLATION_Y_TO = 0f

    fun animateProfileScreen(binding: FragmentProfileBinding) {
        val iconAnimatorListener = object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val scaleName = ObjectAnimator.ofFloat(binding.ivName, "scaleX", SCALE_X_TO)
                    .apply { duration = DURATION }
                val scaleMobile = ObjectAnimator.ofFloat(binding.ivMobileNo, "scaleX", SCALE_X_TO)
                    .apply { duration = DURATION }
                val scaleAddress = ObjectAnimator.ofFloat(binding.ivAddress, "scaleX", SCALE_X_TO)
                    .apply { duration = DURATION }
                val animatorSet = AnimatorSet()
                animatorSet.play(scaleName)
                    .with(scaleMobile)
                    .with(scaleAddress)
                animatorSet.start()
            }
        }

        val textAnimatorListener = object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                val translateNameTitle = ObjectAnimator.ofFloat(binding.tvTitleName, "translationX", TRANSLATION_X_TO)
                    .apply { duration = DURATION }
                val translateNameDesc = ObjectAnimator.ofFloat(binding.tvDescriptionName, "translationX", TRANSLATION_X_TO)
                    .apply { duration = DURATION }
                val translateMobileTitle = ObjectAnimator.ofFloat(binding.tvTitleMobileNo, "translationX", TRANSLATION_X_TO)
                    .apply { duration = DURATION }
                val translateMobileDesc = ObjectAnimator.ofFloat(binding.tvDescriptionMobileNo, "translationX", TRANSLATION_X_TO)
                    .apply { duration = DURATION }
                val translateAddressTitle = ObjectAnimator.ofFloat(binding.tvTitleAddress, "translationX", TRANSLATION_X_TO)
                    .apply { duration = DURATION }
                val translateAddressDesc = ObjectAnimator.ofFloat(binding.tvDescriptionAddress, "translationX", TRANSLATION_X_TO)
                    .apply { duration = DURATION }
                val animatorSet = AnimatorSet()
                animatorSet.addListener(iconAnimatorListener)
                animatorSet.play(translateNameTitle)
                    .with(translateNameDesc)
                    .with(translateMobileTitle)
                    .with(translateMobileDesc)
                    .with(translateAddressTitle)
                    .with(translateAddressDesc)
                animatorSet.start()
            }
        }

        binding.cvProfileScreen.clearAnimation()
        binding.cvProfileScreen.translationY = TRANSLATION_Y_FROM
        binding.ivName.scaleX = SCALE_X_FROM
        binding.ivMobileNo.scaleX = SCALE_X_FROM
        binding.ivAddress.scaleX = SCALE_X_FROM
        binding.tvTitleAddress.translationX = TRANSLATION_X_FROM
        binding.tvDescriptionAddress.translationX = TRANSLATION_X_FROM
        binding.tvTitleMobileNo.translationX = TRANSLATION_X_FROM
        binding.tvDescriptionMobileNo.translationX = TRANSLATION_X_FROM
        binding.tvTitleName.translationX = TRANSLATION_X_FROM
        binding.tvDescriptionName.translationX = TRANSLATION_X_FROM
        binding.cvProfileScreen.animate()
            .translationY(TRANSLATION_Y_TO)
            .setDuration(DURATION*2)
            .setListener(textAnimatorListener)
            .start()
    }

}
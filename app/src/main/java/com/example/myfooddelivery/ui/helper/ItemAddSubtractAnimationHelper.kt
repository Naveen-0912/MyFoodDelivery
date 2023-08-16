package com.example.myfooddelivery.ui.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.databinding.MenuItemBinding

object ItemAddSubtractAnimationHelper {

    private fun removeItemIconAnimator(imageView: ImageView?, isNightMode: Boolean): AnimatorSet {
        val scaleDownFrom = 1.2f
        val scaleDownTo = 1f
        val scaleUpFrom = 1f
        val scaleUpTo = 1.2f
        val rotateStart = 0f
        val rotateEnd = -50f

        if (imageView == null)
            return AnimatorSet()
        val scaleDownCartX = ObjectAnimator.ofFloat(imageView, "scaleX", scaleDownFrom, scaleDownTo)
        val scaleDownCartY = ObjectAnimator.ofFloat(imageView, "scaleY", scaleDownFrom, scaleDownTo)
            .apply {
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        scaleDownCartX.start()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (isNightMode)
                            imageView.setImageResource(R.drawable.cart_dark_mode)
                        else
                            imageView.setImageResource(R.drawable.cart)
                    }
                })
            }
        val scaleUpCartX = ObjectAnimator.ofFloat(imageView, "scaleX", scaleUpFrom, scaleUpTo)
        val scaleUpCartY = ObjectAnimator.ofFloat(imageView, "scaleY", scaleUpFrom, scaleUpTo)
            .apply {
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        scaleUpCartX.start()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        imageView.setImageResource(R.drawable.remove_from_cart)
                    }
                })
            }
        val rotateCartRemove = ObjectAnimator.ofFloat(imageView, "rotation", rotateStart, rotateEnd, rotateStart)
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(scaleUpCartY, rotateCartRemove, scaleDownCartY)
        return animatorSet
    }

    private fun addItemIconAnimator(imageView: ImageView?, isNightMode: Boolean): AnimatorSet {
        val scaleDownCartFrom = 1f
        val scaleDownCartTo = 0f
        val scaleUpCartFrom = 0.8f
        val scaleUpCartTo = 1f
        val scaleDownAddToCartFrom = 1.5f
        val scaleDownAddToCartTo = 1f
        val scaleUpAddToCartFrom = 0f
        val scaleUpAddToCartTo = 1.5f

        if (imageView == null)
            return AnimatorSet()
        val scaleDownCartY = ObjectAnimator.ofFloat(imageView, "scaleY", scaleDownCartFrom, scaleDownCartTo)
            .apply {
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        imageView.setImageResource(R.drawable.add_to_cart)
                    }
                })
            }
        val scaleUpCartX = ObjectAnimator.ofFloat(imageView, "scaleX", scaleUpCartFrom, scaleUpCartTo)
        val scaleUpCartY = ObjectAnimator.ofFloat(imageView, "scaleY", scaleUpCartFrom, scaleUpCartTo)
            .apply {
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        scaleUpCartX.start()
                    }
                })
            }
        val scaleDownAddToCartX = ObjectAnimator.ofFloat(imageView, "scaleX", scaleDownAddToCartFrom, scaleDownAddToCartTo)
        val scaleDownAddToCartY = ObjectAnimator.ofFloat(imageView, "scaleY", scaleDownAddToCartFrom, scaleDownAddToCartTo)
            .apply {
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        scaleDownAddToCartX.start()
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (isNightMode)
                            imageView.setImageResource(R.drawable.cart_dark_mode)
                        else
                            imageView.setImageResource(R.drawable.cart)
                    }
                })
            }
        val scaleUpAddToCartY = ObjectAnimator.ofFloat(imageView, "scaleY", scaleUpAddToCartFrom, scaleUpAddToCartTo)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(scaleDownCartY, scaleUpAddToCartY, scaleDownAddToCartY, scaleUpCartY)
        return animatorSet
    }

    fun itemFirstAddAnimation(binding: MenuItemBinding, imageView: ImageView, isNightMode: Boolean) {
        val animationDuration = 200L
        val addTranslationXFrom = 50f
        val subTranslationXFrom = -50f
        val translationXTo = 0f
        val noOfItemsScaleXFrom = 0f
        val noOfItemsScaleXTo = 1f
        val addBtnScaleX = 0f

        binding.imgBtnAdd.translationX = addTranslationXFrom
        binding.imgBtnSubtract.translationX = subTranslationXFrom
        binding.tvNoOfItems.scaleX = noOfItemsScaleXFrom
        val addBtnAnimator = ObjectAnimator.ofFloat(binding.imgBtnAdd, "translationX", translationXTo)
            .apply { duration = animationDuration }
        val subtractBtnAnimator = ObjectAnimator.ofFloat(binding.imgBtnSubtract, "translationX", translationXTo)
            .apply { duration = animationDuration }
        val noOfItemsAnimator = ObjectAnimator.ofFloat(binding.tvNoOfItems, "scaleX", noOfItemsScaleXTo)
            .apply { duration = animationDuration }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(addBtnAnimator, subtractBtnAnimator, noOfItemsAnimator)

        val btnAddItemAnimator = ObjectAnimator.ofFloat(binding.btnAddItem, "scaleX", addBtnScaleX)
            .apply { duration = animationDuration }
        btnAddItemAnimator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.btnAddItem.visibility = View.INVISIBLE
                binding.btnAddItem.isClickable = false
                binding.cvForItemAddSubtractButton.visibility = View.VISIBLE
                binding.cvForItemAddSubtractButton.isClickable = true
                animatorSet.start()
            }
        })
        AnimatorSet().apply {
            playSequentially(btnAddItemAnimator, addItemIconAnimator(imageView, isNightMode))
            start()
        }
    }

    fun itemLastRemoveAnimation(binding: MenuItemBinding, imageView: ImageView, isNightMode: Boolean) {
        val addBtnDuration = 100L
        val addItemDuration = 100L
        val btnAddTranslationX = 50f
        val btnSubTranslationX = -50f
        val noOfItemsScaleX = 0f
        val addItemScaleX = 1f

        val addBtnAnimator = ObjectAnimator.ofFloat(binding.imgBtnAdd, "translationX", btnAddTranslationX)
            .apply { duration = addBtnDuration }
        val subtractBtnAnimator = ObjectAnimator.ofFloat(binding.imgBtnSubtract, "translationX", btnSubTranslationX)
            .apply { duration = addBtnDuration }
        val noOfItemsAnimator = ObjectAnimator.ofFloat(binding.tvNoOfItems, "scaleX", noOfItemsScaleX)
            .apply { duration = addBtnDuration }
        val animatorSet = AnimatorSet()

        val btnAddItemAnimator = ObjectAnimator.ofFloat(binding.btnAddItem, "scaleX", addItemScaleX)
            .apply { duration = addItemDuration }

        animatorSet.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                binding.btnAddItem.visibility = View.VISIBLE
                binding.btnAddItem.isClickable = true
                binding.cvForItemAddSubtractButton.visibility = View.INVISIBLE
                binding.cvForItemAddSubtractButton.isClickable = false
                btnAddItemAnimator.start()
            }
        })
        animatorSet.playTogether(addBtnAnimator, subtractBtnAnimator, noOfItemsAnimator)
        val iconAnimatorSet = removeItemIconAnimator(imageView, isNightMode)
        AnimatorSet().apply {
            playTogether(iconAnimatorSet, animatorSet)
            start()
        }
    }

    fun itemAddRemoveAnimation(noOfItems: TextView, imageView: ImageView?, isItemAdded: Boolean, isNightMode: Boolean) {
        val translateOutStart = 0f
        val translateOutAbove = -100f
        val translateOutBelow = 100f

        val translateOut = if (isItemAdded) ObjectAnimator.ofFloat(noOfItems, "translationY", translateOutStart, translateOutAbove)
            else ObjectAnimator.ofFloat(noOfItems, "translationY", translateOutStart, translateOutBelow)
        val translateToOriginalPosition = if (isItemAdded) ObjectAnimator.ofFloat(noOfItems, "translationY", translateOutBelow, translateOutStart)
            else ObjectAnimator.ofFloat(noOfItems, "translationY", translateOutAbove, translateOutStart)

        translateOut.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                noOfItems.text = if (isItemAdded) "${noOfItems.text.toString().toInt() + 1}"
                    else "${noOfItems.text.toString().toInt() - 1}"
                translateToOriginalPosition.start()
            }
        })

        AnimatorSet().apply {
            if (isItemAdded)
                playTogether(translateOut, addItemIconAnimator(imageView, isNightMode))
            else
                playTogether(translateOut, removeItemIconAnimator(imageView, isNightMode))
            start()
        }
    }
}
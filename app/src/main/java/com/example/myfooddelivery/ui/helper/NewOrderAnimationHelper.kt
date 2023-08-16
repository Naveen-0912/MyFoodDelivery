package com.example.myfooddelivery.ui.helper

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.example.myfooddelivery.databinding.FragmentNewOrderBinding

object NewOrderAnimationHelper {

    fun showBackButton(binding: FragmentNewOrderBinding) {
        if (binding.btnFilter.visibility == View.VISIBLE) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.clCustomerOrder)
            constraintSet.setVisibility(binding.btnBackNestedScrollView.id, View.VISIBLE)
            TransitionManager.beginDelayedTransition(binding.clCustomerOrder)
            constraintSet.applyTo(binding.clCustomerOrder)
        }
    }

    fun hideBackButton(binding: FragmentNewOrderBinding) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clCustomerOrder)
        constraintSet.setVisibility(binding.btnBackNestedScrollView.id, View.GONE)
        TransitionManager.beginDelayedTransition(binding.clCustomerOrder)
        constraintSet.applyTo(binding.clCustomerOrder)
    }

    fun hideFilterOptions(binding: FragmentNewOrderBinding) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clCustomerOrder)
        constraintSet.setVisibility(binding.btnBackNestedScrollView.id, View.GONE)
        constraintSet.setVisibility(binding.searchBar.cvForSearchBar.id, View.GONE)
        constraintSet.setVisibility(binding.btnFavorite.id, View.GONE)
        constraintSet.setVisibility(binding.tvFilter.id, View.GONE)
        constraintSet.setVisibility(binding.tvSort.id, View.GONE)
        constraintSet.setVisibility(binding.btnFilter.id, View.GONE)
        constraintSet.setVisibility(binding.btnSort.id, View.GONE)
        constraintSet.clear(binding.rvOrderItemList.id, ConstraintSet.TOP)
        constraintSet.connect(binding.rvOrderItemList.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        TransitionManager.beginDelayedTransition(binding.clCustomerOrder)
        constraintSet.applyTo(binding.clCustomerOrder)
    }

    fun showFilterOptions(binding: FragmentNewOrderBinding) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clCustomerOrder)
        constraintSet.clear(binding.rvOrderItemList.id, ConstraintSet.TOP)
        constraintSet.connect(binding.rvOrderItemList.id, ConstraintSet.TOP, binding.tvFilter.id, ConstraintSet.BOTTOM)
        constraintSet.setVisibility(binding.searchBar.cvForSearchBar.id, View.VISIBLE)
        constraintSet.setVisibility(binding.btnBackNestedScrollView.id, View.VISIBLE)
        constraintSet.setVisibility(binding.btnFavorite.id, View.VISIBLE)
        constraintSet.setVisibility(binding.tvFilter.id, View.VISIBLE)
        constraintSet.setVisibility(binding.tvSort.id, View.VISIBLE)
        constraintSet.setVisibility(binding.btnFilter.id, View.VISIBLE)
        constraintSet.setVisibility(binding.btnSort.id, View.VISIBLE)
        TransitionManager.beginDelayedTransition(binding.clCustomerOrder)
        binding.tvFilter.setPadding(5,10,5,10)
        constraintSet.applyTo(binding.clCustomerOrder)
    }
}
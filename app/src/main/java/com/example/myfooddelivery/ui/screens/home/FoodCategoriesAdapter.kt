package com.example.myfooddelivery.ui.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.databinding.FoodCategoryCardBinding
import com.example.myfooddelivery.constants.FoodCategories

class FoodCategoriesAdapter(private var foodCategories: List<FoodCategories>) :
    RecyclerView.Adapter<FoodCategoriesAdapter.HotelViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class HotelViewHolder(val binding: FoodCategoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, category: FoodCategories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val binding: FoodCategoryCardBinding = FoodCategoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HotelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return foodCategories.size
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val curCategory = foodCategories[position]
        with(holder) {
            binding.ivCategory.setImageResource(curCategory.categoryWrapper.drawableId)
            binding.tvCategory.text = curCategory.categoryWrapper.name
            binding.cvRoot.setOnClickListener {
                onClickListener?.onClick(position, curCategory)
            }
        }
    }
}

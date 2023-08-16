package com.example.myfooddelivery.ui.screens.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.databinding.SearchCardBinding

class FoodItemsAdapter(var items: List<Item>, private val context: Context) :
    RecyclerView.Adapter<FoodItemsAdapter.FoodItemViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class FoodItemViewHolder(val binding: SearchCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding:SearchCardBinding = SearchCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val curItem = items[position]
        with(holder) {
            binding.ivPicture.setImageResource(curItem.imageId)
            binding.tvName.text = curItem.itemName
            binding.tvInformation.text = context.resources.getString(R.string.item_price, curItem.itemPrice)
            binding.tvRating.text = if (curItem.ratings.averageRatings < 0) context.resources.getString(R.string.empty_ratings)
                else curItem.ratings.averageRatings.toString()
            binding.cvRoot.setOnClickListener {
                    onClickListener?.onClick(position, curItem)
            }
        }
    }
}
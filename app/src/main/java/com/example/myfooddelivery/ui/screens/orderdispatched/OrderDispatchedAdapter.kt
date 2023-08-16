package com.example.myfooddelivery.ui.screens.orderdispatched

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.databinding.OrderSummaryCardBinding

class OrderDispatchedAdapter(var itemsWithQuantityWrapper: List<ItemWithQuantityWrapper>,
                          private val context: Context
) :
    RecyclerView.Adapter<OrderDispatchedAdapter.FoodItemViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class FoodItemViewHolder(val binding: OrderSummaryCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding: OrderSummaryCardBinding = OrderSummaryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsWithQuantityWrapper.size
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val curItemWrapper = itemsWithQuantityWrapper[position]
        with(holder) {
            binding.tvItemName.text = curItemWrapper.item.itemName
            binding.tvNoOfItems.text = curItemWrapper.quantity.toString()
            binding.tvItemTotalPrice.text = context.resources.getString(R.string.item_price, curItemWrapper.item.itemPrice * curItemWrapper.quantity)
            if (curItemWrapper.item.isVeg)
                binding.ivItemType.setImageResource(R.drawable.veg_indicator)
            else
                binding.ivItemType.setImageResource(R.drawable.non_veg_indicator)
            binding.imgBtnAdd.visibility = View.INVISIBLE
            binding.imgBtnSubtract.visibility = View.INVISIBLE
            itemView.setOnClickListener {
                onClickListener?.onClick(curItemWrapper.item)
            }
        }
    }
}
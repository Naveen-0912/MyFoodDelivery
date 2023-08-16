package com.example.myfooddelivery.ui.screens.ordersummary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.databinding.OrderSummaryCardBinding
import com.example.myfooddelivery.ui.helper.ItemAddSubtractAnimationHelper

class OrderSummaryAdapter(var itemsWithQuantityWrapper: MutableList<ItemWithQuantityWrapper>,
                          private val viewModel: OrderSummaryViewModel,
                          private val context: Context) :
    RecyclerView.Adapter<OrderSummaryAdapter.FoodItemViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class FoodItemViewHolder(val binding: OrderSummaryCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding:OrderSummaryCardBinding = OrderSummaryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsWithQuantityWrapper.size
    }

    private fun setUpItemAddButton(binding: OrderSummaryCardBinding, curItemWrapper: ItemWithQuantityWrapper) {
        binding.imgBtnAdd.setOnClickListener {
            viewModel.addItemToOrder(curItemWrapper.item)
            val itemWithQuantityWrapper = viewModel.itemWithQuantityWrapperList.find{curItemWrapper.item == it.item}
            binding.tvItemTotalPrice.text = context.resources.getString(R.string.item_price, itemWithQuantityWrapper?.item?.itemPrice!! * itemWithQuantityWrapper.quantity)
            ItemAddSubtractAnimationHelper.itemAddRemoveAnimation(binding.tvNoOfItems, null,true, isNightMode = false)
        }
    }

    private fun setUpItemDeleteButton(binding: OrderSummaryCardBinding, curItemWrapper: ItemWithQuantityWrapper, position: Int) {
        binding.imgBtnSubtract.setOnClickListener {
            viewModel.removeItemFromOrder(curItemWrapper.item)
            val itemWithQuantityWrapper = viewModel.itemWithQuantityWrapperList.find{curItemWrapper.item == it.item}
            if (itemWithQuantityWrapper == null) {
                itemsWithQuantityWrapper.remove(curItemWrapper)
                notifyItemRemoved(position)
            }
            else {
                binding.tvItemTotalPrice.text = context.resources.getString(R.string.item_price, itemWithQuantityWrapper.item.itemPrice * itemWithQuantityWrapper.quantity)
                ItemAddSubtractAnimationHelper.itemAddRemoveAnimation(
                    binding.tvNoOfItems,
                    null,
                    false,
                    isNightMode = false
                )
            }
            if (viewModel.itemWithQuantityWrapperList.isEmpty())
                viewModel.isOrderedItemsEmpty.value = true
        }
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
            setUpItemAddButton(binding, curItemWrapper)
            setUpItemDeleteButton(binding, curItemWrapper, position)
            itemView.setOnClickListener {
                onClickListener?.onClick(position, curItemWrapper.item)
            }
        }
    }
}
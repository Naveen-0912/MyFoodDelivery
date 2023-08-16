package com.example.myfooddelivery.ui.screens.particularorder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.db.entities.ItemWithQuantityWrapper
import com.example.myfooddelivery.databinding.MenuItemBinding
import com.example.myfooddelivery.constants.FoodType

class ViewParticularOrderAdapter(var itemsWithQuantityWrapper: List<ItemWithQuantityWrapper>,
private val context: Context) :
    RecyclerView.Adapter<ViewParticularOrderAdapter.FoodItemViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class FoodItemViewHolder(val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding: MenuItemBinding = MenuItemBinding.inflate(
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
            binding.btnAddItem.visibility = View.GONE
            binding.cvForItemAddSubtractButton.visibility = View.GONE
            binding.tvItemTitle.text = curItemWrapper.item.itemName
            binding.tvItemPrice.text = context.resources.getString(R.string.item_price, curItemWrapper.item.itemPrice)
            binding.tvItemRating.text = context.resources.getString(R.string.item_quantity, curItemWrapper.quantity)
            binding.tvItemRating.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0)
            binding.ivItemPicture.setImageResource(curItemWrapper.item.imageId)
            if(curItemWrapper.item.isVeg) {
                binding.ivItemType.setImageResource(R.drawable.veg_indicator)
                binding.tvItemType.text =  FoodType.VEG.value
                binding.tvItemType.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            else {
                binding.ivItemType.setImageResource(R.drawable.non_veg_indicator)
                binding.tvItemType.text = FoodType.NON_VEG.value
                binding.tvItemType.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            if (curItemWrapper == itemsWithQuantityWrapper.last())
                binding.divider.visibility = View.INVISIBLE
            itemView.setOnClickListener {
                onClickListener?.onClick(curItemWrapper.item)
            }
        }
    }
}
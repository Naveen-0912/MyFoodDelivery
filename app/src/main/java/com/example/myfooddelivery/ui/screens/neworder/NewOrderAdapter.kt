package com.example.myfooddelivery.ui.screens.neworder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.databinding.ClearCartDialogBoxBinding
import com.example.myfooddelivery.databinding.FragmentNewOrderBinding
import com.example.myfooddelivery.databinding.MenuItemBinding
import com.example.myfooddelivery.constants.FoodType
import com.example.myfooddelivery.ui.helper.ItemAddSubtractAnimationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewOrderAdapter(var items: List<Item>,
                      private val viewModel: NewOrderViewModel,
                      private val fragmentBinding: FragmentNewOrderBinding,
                      private val context: Context) :
    RecyclerView.Adapter<NewOrderAdapter.FoodItemViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class FoodItemViewHolder(val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, item: Item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val binding:MenuItemBinding = MenuItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    private fun addItemToCart(binding: MenuItemBinding, curItem: Item) {
        viewModel.addItemToOrder(curItem)
        binding.tvNoOfItems.text = viewModel.itemsWithQuantity[curItem].toString()
        ItemAddSubtractAnimationHelper.itemFirstAddAnimation(
            binding,
            fragmentBinding.viewCardBottomSheet.ivCart,
            context.resources.configuration.isNightModeActive
        )
    }

    private fun getClearCartConfirmation(binding: MenuItemBinding, curItem: Item) {
        val style = if (context.resources.configuration.isNightModeActive)
            R.style.GeneralAlertDialogLight
        else
            R.style.GeneralAlertDialogDark
        val dialogBoxBinding = ClearCartDialogBoxBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context, style)
            .setView(dialogBoxBinding.root)
            .create()
        dialogBoxBinding.btnNo.setOnClickListener {
            viewModel.viewModelScope.launch {
                delay(250)
                alertDialog.dismiss()
            }
        }
        dialogBoxBinding.btnReplace.setOnClickListener {
            viewModel.viewModelScope.launch {
                delay(250)
                alertDialog.dismiss()
            }
            addItemToCart(binding, curItem)
        }
        alertDialog.show()
    }

    private fun setUpItemAddButton(binding: MenuItemBinding, curItem: Item) {
        binding.btnAddItem.setOnClickListener {
            if (viewModel.totalItemsSelected == 0 || viewModel.itemsWithQuantity.isNotEmpty())
                addItemToCart(binding, curItem)
            else
                getClearCartConfirmation(binding, curItem)
        }
        binding.imgBtnAdd.setOnClickListener {
            viewModel.addItemToOrder(curItem)
            ItemAddSubtractAnimationHelper.itemAddRemoveAnimation(
                binding.tvNoOfItems,
                fragmentBinding.viewCardBottomSheet.ivCart,
                true,
                context.resources.configuration.isNightModeActive
            )
        }
    }

    private fun setUpItemDeleteButton(binding: MenuItemBinding, curItem: Item) {
        binding.imgBtnSubtract.setOnClickListener {
            viewModel.removeItemFromOrder(curItem)
            if (viewModel.itemsWithQuantity.containsKey(curItem)) {
                ItemAddSubtractAnimationHelper.itemAddRemoveAnimation(
                    binding.tvNoOfItems,
                    fragmentBinding.viewCardBottomSheet.ivCart,
                    false,
                    context.resources.configuration.isNightModeActive
                )
            }
            else
                ItemAddSubtractAnimationHelper.itemLastRemoveAnimation(
                    binding,
                    fragmentBinding.viewCardBottomSheet.ivCart,
                    context.resources.configuration.isNightModeActive
                )
        }
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val curItem = items[position]
        with(holder) {
            if (viewModel.itemsWithQuantity.contains(curItem)) {
                binding.btnAddItem.visibility = View.INVISIBLE
                binding.btnAddItem.isClickable = false
                binding.cvForItemAddSubtractButton.visibility = View.VISIBLE
                binding.cvForItemAddSubtractButton.isClickable = true
                binding.tvNoOfItems.text = viewModel.itemsWithQuantity[curItem].toString()
            }
            binding.tvItemTitle.text = curItem.itemName
            binding.tvItemPrice.text = context.resources.getString(R.string.item_price, curItem.itemPrice)
            binding.tvItemRating.text = if (curItem.ratings.averageRatings < 0)
                    context.resources.getString(R.string.average_ratings_with_total_ratings, 0.0f, curItem.ratings.totalNoOfRatings)
                else
                context.resources.getString(R.string.average_ratings_with_total_ratings, curItem.ratings.averageRatings, curItem.ratings.totalNoOfRatings)
            binding.ivItemPicture.setImageResource(curItem.imageId)
            if(curItem.isVeg){
                binding.ivItemType.setImageResource(R.drawable.veg_indicator)
                binding.tvItemType.text = FoodType.VEG.value
                binding.tvItemType.setTextColor(ContextCompat.getColor(context, R.color.green))
            }
            else{
                binding.ivItemType.setImageResource(R.drawable.non_veg_indicator)
                binding.tvItemType.text = FoodType.NON_VEG.value
                binding.tvItemType.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
            if (curItem == items.last())
                binding.divider.visibility = View.INVISIBLE
            binding.tvNoOfItems.setOnClickListener {}
            setUpItemAddButton(binding, curItem)
            setUpItemDeleteButton(binding, curItem)
            itemView.setOnClickListener {
                onClickListener?.onClick(position, curItem)
            }
        }
    }
}
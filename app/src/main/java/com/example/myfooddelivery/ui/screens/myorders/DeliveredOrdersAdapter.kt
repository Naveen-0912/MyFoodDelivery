package com.example.myfooddelivery.ui.screens.myorders

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.databinding.OrderCardBinding
import com.example.myfooddelivery.ui.helper.FavoriteAnimationHelper
import kotlinx.coroutines.launch

class DeliveredOrdersAdapter(var orders: List<Order>,
                             private val viewModel: ViewOrdersViewModel,
                             private val context: Context
) : RecyclerView.Adapter<DeliveredOrdersAdapter.OrderItemViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onLikeClickListener: OnLikeClickListener? = null

    inner class OrderItemViewHolder(val binding: OrderCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding: OrderCardBinding = OrderCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderItemViewHolder(binding)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setOnLikeClickListener(onLikeClickListener: OnLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, order: Order)
    }
    interface OnLikeClickListener {
        fun onClick(hotelId: Int, isLiked: Boolean, sameHotelsPosition: List<Int>)
    }

    override fun getItemId(position: Int): Long {
        return orders[position].orderId.toLong()
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    private fun likeButtonListener(isLiked: Boolean, imageButton: ImageButton, hotelId: Int) {
        if (isLiked)
            imageButton.setImageResource(R.drawable.baseline_favorite_24)
        else
            if (context.resources.configuration.isNightModeActive)
                imageButton.setImageResource(R.drawable.baseline_favorite_border_24)
            else
                imageButton.setImageResource(R.drawable.baseline_favorite_border_24_dark)
        FavoriteAnimationHelper.favoriteChangedAnimation(imageButton)
        onLikeClickListener?.onClick(hotelId, isLiked, orders.filter { it.hotelId == hotelId }.map { orders.indexOf(it) })
    }

    private fun setRateButtonListener(binding: OrderCardBinding, order: Order) {
        binding.btnRate.setOnClickListener {
            viewModel.viewModelScope.launch {
                if(viewModel.rateOrder(order, binding.ratingBar.rating)) {
                    orders = viewModel.getDeliveredOrders()
                    orders.forEachIndexed { index, orderForEach ->
                        if (orderForEach.hotelId == viewModel.updatedHotelId)
                            notifyItemChanged(index)
                    }
                }
            }
        }
    }

    private fun updateRatingsBarConstraintsWithButtonGone(binding: OrderCardBinding) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clForOrderCard)
        constraintSet.connect(binding.ratingBar.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.applyTo(binding.clForOrderCard)
    }

    private fun updateRatingsBarConstraintsWithButtonVisible(binding: OrderCardBinding) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clForOrderCard)
        constraintSet.connect(binding.ratingBar.id, ConstraintSet.END, binding.btnRate.id, ConstraintSet.START)
        constraintSet.applyTo(binding.clForOrderCard)
    }

    private fun setUpRatingsBar(binding: OrderCardBinding, order: Order) {
        binding.ratingBar.rating = if (order.rating < 0) 0f else order.rating
        if(order.rating > -1.0f) {
            binding.btnRate.visibility = View.GONE
            binding.ratingBar.setIsIndicator(true)//user cant modify value
            updateRatingsBarConstraintsWithButtonGone(binding)
        }
        else {
            updateRatingsBarConstraintsWithButtonVisible(binding)
            binding.btnRate.visibility = View.VISIBLE
            binding.ratingBar.setIsIndicator(false)
            setRateButtonListener(binding, order)
        }
    }

    private fun setUpCardDetailsPortrait(binding: OrderCardBinding, position: Int) {
        binding.hotelCardLandscape.clForItemCard.visibility = View.GONE
        binding.hotelCardPortrait.clForItemCard.visibility = View.VISIBLE
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.connect(binding.ratingBar.id, ConstraintSet.TOP, binding.hotelCardPortrait.clForItemCard.id, ConstraintSet.BOTTOM)
        constraintSet.applyTo(binding.root)
        val curOrder = orders[position]
        viewModel.viewModelScope.launch {
            var isLiked = curOrder.hotelId in viewModel.getCustomerAccount()?.favoriteHotels!!
            val hotelAccount = viewModel.getHotelById(curOrder.hotelId)!!
            binding.hotelCardPortrait.ivHotelPicture.setImageResource(hotelAccount.imageId)
            binding.hotelCardPortrait.tvHotelName.text = hotelAccount.name
            binding.hotelCardPortrait.tvInformation.text = context.resources.getString(R.string.item_price, curOrder.itemsWithQuantity.sumOf { it.item.itemPrice*it.quantity })
            binding.hotelCardPortrait.tvHotelRating.text = if (hotelAccount.ratings.averageRatings < 0) context.resources.getString(R.string.empty_ratings)
                else hotelAccount.ratings.averageRatings.toString()
            if (isLiked)
                binding.hotelCardPortrait.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
            else {
                if (context.resources.configuration.isNightModeActive)
                    binding.hotelCardPortrait.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                else
                    binding.hotelCardPortrait.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24_dark)
            }
            binding.hotelCardPortrait.btnFavorite.setOnClickListener {
                isLiked = !isLiked
                likeButtonListener(isLiked, binding.hotelCardPortrait.btnFavorite, curOrder.hotelId)
            }
            if (hotelAccount.isVeg)
                binding.hotelCardPortrait.ivHotelType.setImageResource(R.drawable.veg_indicator)
            else
                binding.hotelCardPortrait.ivHotelType.setImageResource(R.drawable.non_veg_indicator)
            setUpRatingsBar(binding, curOrder)
        }
        binding.hotelCardPortrait.cvRoot.setOnClickListener {
            onClickListener?.onClick(position, curOrder)
        }
    }

    private fun setUpCardDetailsLandscape(binding: OrderCardBinding, position: Int) {
        binding.hotelCardPortrait.clForItemCard.visibility = View.GONE
        binding.hotelCardLandscape.clForItemCard.visibility = View.VISIBLE
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.connect(binding.ratingBar.id, ConstraintSet.TOP, binding.hotelCardLandscape.clForItemCard.id, ConstraintSet.BOTTOM)
        constraintSet.applyTo(binding.root)
        val curOrder = orders[position]
        viewModel.viewModelScope.launch {
            var isLiked = curOrder.hotelId in viewModel.getCustomerAccount()?.favoriteHotels!!
            val hotelAccount = viewModel.getHotelById(curOrder.hotelId)!!
            binding.hotelCardLandscape.ivHotelPicture.setImageResource(hotelAccount.imageId)
            binding.hotelCardLandscape.tvHotelName.text = hotelAccount.name
            binding.hotelCardLandscape.tvInformation.text = context.resources.getString(R.string.item_price, curOrder.itemsWithQuantity.sumOf { it.item.itemPrice*it.quantity })
            binding.hotelCardLandscape.tvHotelRating.text = if (hotelAccount.ratings.averageRatings < 0) context.resources.getString(R.string.empty_ratings)
            else hotelAccount.ratings.averageRatings.toString()
            if (isLiked)
                binding.hotelCardLandscape.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
            else {
                if (context.resources.configuration.isNightModeActive)
                    binding.hotelCardLandscape.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                else
                    binding.hotelCardLandscape.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24_dark)
            }
            binding.hotelCardLandscape.btnFavorite.setOnClickListener {
                isLiked = !isLiked
                likeButtonListener(isLiked, binding.hotelCardLandscape.btnFavorite, curOrder.hotelId)
            }
            if (hotelAccount.isVeg)
                binding.hotelCardLandscape.ivHotelType.setImageResource(R.drawable.veg_indicator)
            else
                binding.hotelCardLandscape.ivHotelType.setImageResource(R.drawable.non_veg_indicator)
            setUpRatingsBar(binding, curOrder)
        }
        binding.hotelCardLandscape.cvRoot.setOnClickListener {
            onClickListener?.onClick(position, curOrder)
        }
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            setUpCardDetailsPortrait(holder.binding, position)
        else
            setUpCardDetailsLandscape(holder.binding, position)
    }
}
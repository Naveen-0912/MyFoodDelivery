package com.example.myfooddelivery.ui.screens.myorders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.databinding.QuickFindHotelFoodCardBinding
import kotlinx.coroutines.launch

class ActiveCancelledOrdersAdapter(var orders: List<Order>, private val viewModel: ViewOrdersViewModel, private val context: Context) :
    RecyclerView.Adapter<ActiveCancelledOrdersAdapter.HotelViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class HotelViewHolder(val binding: QuickFindHotelFoodCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, order: Order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val binding: QuickFindHotelFoodCardBinding = QuickFindHotelFoodCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HotelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {

        val curOrder = orders[position]
        viewModel.viewModelScope.launch {
            val hotelAccount = viewModel.getHotelById(curOrder.hotelId)!!
            holder.binding.ivHotelPicture.setImageResource(hotelAccount.imageId)
            holder.binding.tvHotelName.text = hotelAccount.name
        }
        holder.binding.tvAdditionalInformation.text = context.resources.getString(R.string.item_price, curOrder.itemsWithQuantity.sumOf { it.item.itemPrice*it.quantity })
        holder.binding.ivRatings.visibility = View.INVISIBLE
        holder.binding.tvHotelRating.visibility = View.INVISIBLE
        holder.binding.cvRoot.setOnClickListener {
            onClickListener?.onClick(position, curOrder)
        }
    }
}
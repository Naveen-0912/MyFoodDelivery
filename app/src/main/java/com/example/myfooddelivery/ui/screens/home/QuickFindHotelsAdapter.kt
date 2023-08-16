package com.example.myfooddelivery.ui.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.databinding.QuickFindHotelFoodCardBinding

class QuickFindHotelsAdapter(var hotels: List<HotelAccount>, private val context: Context) :
    RecyclerView.Adapter<QuickFindHotelsAdapter.HotelViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class HotelViewHolder(val binding: QuickFindHotelFoodCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, hotel: HotelAccount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val binding:QuickFindHotelFoodCardBinding = QuickFindHotelFoodCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HotelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hotels.size
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        val curHotel = hotels[position]
        with(holder) {
            binding.ivHotelPicture.setImageResource(curHotel.imageId)
            binding.tvHotelName.text = curHotel.name
            binding.tvHotelRating.text = if (curHotel.ratings.averageRatings < 0f) context.resources.getString(R.string.empty_ratings)
                else curHotel.ratings.averageRatings.toString()
            binding.tvAdditionalInformation.text = curHotel.address.area
            binding.cvRoot.setOnClickListener {
                onClickListener?.onClick(position, curHotel)
            }
        }
    }
}


package com.example.myfooddelivery.ui.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.databinding.SearchCardBinding

class HotelAdapter(var hotels: List<HotelAccount>) :
    RecyclerView.Adapter<HotelAdapter.HotelViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class HotelViewHolder(val binding: SearchCardBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, hotelAccount: HotelAccount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val binding: SearchCardBinding = SearchCardBinding.inflate(
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
            binding.ivPicture.setImageResource(curHotel.imageId)
            binding.tvName.text = curHotel.name
            binding.tvInformation.text = curHotel.address.area
            binding.tvRating.text = curHotel.ratings.averageRatings.toString()
            binding.cvRoot.setOnClickListener {
                    onClickListener?.onClick(position, curHotel)
            }
        }
    }
}
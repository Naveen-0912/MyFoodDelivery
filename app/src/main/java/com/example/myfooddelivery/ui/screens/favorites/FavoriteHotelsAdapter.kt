package com.example.myfooddelivery.ui.screens.favorites

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.databinding.HotelCardLandscapeBinding
import com.example.myfooddelivery.databinding.HotelCardPortaitBinding
import com.example.myfooddelivery.ui.helper.FavoriteAnimationHelper

class FavoriteHotelsAdapter(var hotels: MutableList<HotelAccount>, private val context: Context) :
    RecyclerView.Adapter<FavoriteHotelsAdapter.HotelViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onLikeClickListener: OnLikeClickListener? = null

    inner class HotelViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setOnLikeClickListener(onLikeClickListener: OnLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener
    }

    interface OnClickListener {
        fun onClick(hotel: HotelAccount)
    }

    interface OnLikeClickListener {
        fun onClick(hotelId: Int, isLiked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelViewHolder {
        val binding:ViewBinding = if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            HotelCardPortaitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        else
            HotelCardLandscapeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotelViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return hotels.size
    }

    private fun likeButtonListener(isLiked: Boolean, imageButton: ImageButton) {
        if (isLiked)
            imageButton.setImageResource(R.drawable.baseline_favorite_24)
        else
            if (context.resources.configuration.isNightModeActive)
                imageButton.setImageResource(R.drawable.baseline_favorite_border_24)
            else
                imageButton.setImageResource(R.drawable.baseline_favorite_border_24_dark)
        FavoriteAnimationHelper.favoriteChangedAnimation(imageButton)
    }

    private fun setUpCardDetailsPortrait(binding: HotelCardPortaitBinding, position: Int) {
        val curHotel = hotels[position]
        var isLiked = true
        binding.ivHotelPicture.setImageResource(curHotel.imageId)
        binding.tvHotelName.text = curHotel.name
        binding.tvHotelRating.text = if (curHotel.ratings.averageRatings < 0f) context.resources.getString(R.string.empty_ratings) else curHotel.ratings.averageRatings.toString()
        binding.tvInformation.text = curHotel.address.area
        binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
        binding.btnFavorite.setOnClickListener {
            isLiked = !isLiked
            likeButtonListener(isLiked, binding.btnFavorite)
            hotels.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, hotels.size-position)
            onLikeClickListener?.onClick(curHotel.hotelId, isLiked)
        }
        if (curHotel.isVeg)
            binding.ivHotelType.setImageResource(R.drawable.veg_indicator)
        else
            binding.ivHotelType.setImageResource(R.drawable.non_veg_indicator)
        binding.cvRoot.setOnClickListener {
            onClickListener?.onClick(curHotel)
        }
    }

    private fun setUpCardDetailsLandscape(binding: HotelCardLandscapeBinding, position: Int) {
        val curHotel = hotels[position]
        var isLiked = true
        binding.ivHotelPicture.setImageResource(curHotel.imageId)
        binding.tvHotelName.text = curHotel.name
        binding.tvHotelRating.text = if (curHotel.ratings.averageRatings < 0f) "0.0" else curHotel.ratings.averageRatings.toString()
        binding.tvInformation.text = curHotel.address.area
        binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
        binding.btnFavorite.setOnClickListener {
            isLiked = !isLiked
            likeButtonListener(isLiked, binding.btnFavorite)
            hotels.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, hotels.size-position)
            onLikeClickListener?.onClick(curHotel.hotelId, isLiked)
        }
        if (curHotel.isVeg)
            binding.ivHotelType.setImageResource(R.drawable.veg_indicator)
        else
            binding.ivHotelType.setImageResource(R.drawable.non_veg_indicator)
        binding.cvRoot.setOnClickListener {
            onClickListener?.onClick(curHotel)
        }
    }

    override fun onBindViewHolder(holder: HotelViewHolder, position: Int) {
        if (holder.binding is HotelCardPortaitBinding)
            setUpCardDetailsPortrait(holder.binding, position)
        else if (holder.binding is HotelCardLandscapeBinding)
            setUpCardDetailsLandscape(holder.binding, position)
    }
}

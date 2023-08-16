package com.example.myfooddelivery.ui.screens.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FragmentFavoritesBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.neworder.NewOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    private val binding: FragmentFavoritesBinding by lazy { FragmentFavoritesBinding.inflate(layoutInflater) }
    private val viewModel: FavoritesViewModel by viewModels { FavoritesViewModelFactory(
        DatabaseProvider(requireContext()),
        LoggedInAccountDataStoreHelper(requireContext())
    ) }
    private val favoritesAdapter: FavoriteHotelsAdapter by lazy { FavoriteHotelsAdapter(mutableListOf(), requireContext()) }

    private fun redirectToOrderFragment(hotelId: Int) {
        requireActivity().supportFragmentManager.commit {
            val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
            requireActivity().supportFragmentManager.setFragmentResult(
                FragmentResultConstants.FRAGMENT_RESULT_KEY.name,
                bundleOf(FragmentResultConstants.HOTEL_ID_KEY.name to hotelId)
            )
            replace(activityBaseBinding.flBaseFragment.id, NewOrderFragment())
            addToBackStack(null)
        }
    }

    private fun setUpAllHotelsRecyclerView() {
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            favoritesAdapter.hotels = viewModel.getFavoriteHotels().toMutableList()
            favoritesAdapter.setOnClickListener(object: FavoriteHotelsAdapter.OnClickListener {
                override fun onClick(hotel: HotelAccount) {
                    redirectToOrderFragment(hotel.hotelId)
                }
            })
            favoritesAdapter.setOnLikeClickListener(object: FavoriteHotelsAdapter.OnLikeClickListener {
                override fun onClick(hotelId: Int, isLiked: Boolean) {
                    lifecycleScope.launch {
                        viewModel.updateFavoriteHotels(hotelId, isLiked)
                        if (favoritesAdapter.hotels.isEmpty()) {
                            binding.ivEmptyFavorites.visibility = View.VISIBLE
                            binding.tvEmptyFavorites.visibility = View.VISIBLE
                        }
                        else {
                            binding.ivEmptyFavorites.visibility = View.INVISIBLE
                            binding.tvEmptyFavorites.visibility = View.INVISIBLE
                        }
                    }
                }
            })
            favoritesAdapter.notifyDataSetChanged()
            binding.rvFavorites.adapter =  favoritesAdapter
            if (favoritesAdapter.hotels.isEmpty()) {
                binding.ivEmptyFavorites.visibility = View.VISIBLE
                binding.tvEmptyFavorites.visibility = View.VISIBLE
            }
            else {
                binding.ivEmptyFavorites.visibility = View.INVISIBLE
                binding.tvEmptyFavorites.visibility = View.INVISIBLE
            }
        }
    }

    private fun showBottomNavBar() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view).visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomNavBar()
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.FAVORITES_SCREEN
        setUpAllHotelsRecyclerView()
    }
}
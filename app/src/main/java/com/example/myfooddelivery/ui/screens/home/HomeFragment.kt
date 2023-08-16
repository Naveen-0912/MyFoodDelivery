package com.example.myfooddelivery.ui.screens.home

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
import com.example.myfooddelivery.databinding.FragmentHomeBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FoodCategories
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.neworder.NewOrderFragment
import com.example.myfooddelivery.ui.screens.search.SearchViewPagerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(
        DatabaseProvider(requireContext()),
        LoggedInAccountDataStoreHelper(requireContext())
        ) }
    private val allHotelsAdapter: AllHotelsAdapter by lazy { AllHotelsAdapter(listOf(), listOf(), requireContext()) }
    private val popularHotelsAdapter: QuickFindHotelsAdapter by lazy { QuickFindHotelsAdapter(listOf(), requireContext()) }
    private val randomHotelsAdapter: QuickFindHotelsAdapter by lazy { QuickFindHotelsAdapter(listOf(), requireContext()) }
    private val foodCategoriesAdapter: FoodCategoriesAdapter by lazy { FoodCategoriesAdapter(FoodCategories.values().toList()) }

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

    private fun redirectToSearchFragment() {
        parentFragmentManager.commit {
            val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
            replace(activityBaseBinding.flBaseFragment.id, SearchViewPagerFragment())
            addToBackStack(null)
        }
    }

    private fun setUpPopularHotelsRecyclerView() {
        binding.rvPopularHotels.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launch {
            popularHotelsAdapter.hotels = viewModel.getPopularHotels()
            popularHotelsAdapter.setOnClickListener(object: QuickFindHotelsAdapter.OnClickListener {
                override fun onClick(position: Int, hotel: HotelAccount) {
                    redirectToOrderFragment(hotel.hotelId)
                }
            })
            popularHotelsAdapter.notifyDataSetChanged()
            binding.rvPopularHotels.adapter =  popularHotelsAdapter
        }
    }

    private fun setUpRandomHotelsRecyclerView() {
        binding.rvRandomHotels.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launch {
            randomHotelsAdapter.hotels = viewModel.getRandomHotels()
            randomHotelsAdapter.setOnClickListener(object : QuickFindHotelsAdapter.OnClickListener {
                override fun onClick(position: Int, hotel: HotelAccount) {
                    redirectToOrderFragment(hotel.hotelId)
                }
            })
            randomHotelsAdapter.notifyDataSetChanged()
            binding.rvRandomHotels.adapter = randomHotelsAdapter
        }
    }

    private fun setUpFoodCategoriesRecyclerView() {
        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        foodCategoriesAdapter.setOnClickListener(object: FoodCategoriesAdapter.OnClickListener {
            override fun onClick(position: Int, category: FoodCategories) {
                viewModel.selectedCategory = category.categoryWrapper.name
                redirectToSearchFragment()
            }
        })
        binding.rvCategories.adapter = foodCategoriesAdapter

    }

    private fun setUpAllHotelsRecyclerView() {
        binding.rvHotels.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            allHotelsAdapter.hotels = viewModel.getAllHotels()
            allHotelsAdapter.favoriteHotels = viewModel.getCustomerAccount()?.favoriteHotels!!
            allHotelsAdapter.setOnClickListener(object: AllHotelsAdapter.OnClickListener {
                override fun onClick(position: Int, hotel: HotelAccount) {
                    redirectToOrderFragment(hotel.hotelId)
                }
            })
            allHotelsAdapter.setOnLikeClickListener(object: AllHotelsAdapter.OnLikeClickListener {
                override fun onClick(hotelId: Int, isLiked: Boolean) {
                    lifecycleScope.launch {
                        viewModel.updateFavoriteHotels(hotelId, isLiked)
                    }
                }
            })
            allHotelsAdapter.notifyDataSetChanged()
            binding.rvHotels.adapter =  allHotelsAdapter
        }
    }

    private fun setUpSearchBar() {
        binding.searchBar.setOnClickListener {
            viewModel.selectedCategory = ""
            redirectToSearchFragment()
        }
    }

    private fun setUpTitle() {
        lifecycleScope.launch {
            val userName = viewModel.getCustomerAccount()?.name
            binding.tvAppTitle.text = resources.getString(R.string.home_welcome_banner, userName)
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
        activityViewModel.selectedMenu = Screens.HOME_SCREEN
        setUpTitle()
        setUpAllHotelsRecyclerView()
        setUpPopularHotelsRecyclerView()
        setUpRandomHotelsRecyclerView()
        setUpFoodCategoriesRecyclerView()
        setUpSearchBar()
    }
}
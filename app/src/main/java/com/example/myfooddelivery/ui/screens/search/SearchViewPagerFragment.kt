package com.example.myfooddelivery.ui.screens.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.databinding.FilterDialogBinding
import com.example.myfooddelivery.databinding.FragmentSearchViewPagerBinding
import com.example.myfooddelivery.databinding.SortDialogFoodBinding
import com.example.myfooddelivery.databinding.SortDialogHotelBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FilterType
import com.example.myfooddelivery.constants.FragmentTags
import com.example.myfooddelivery.constants.SortType
import com.example.myfooddelivery.constants.ViewPagerMenus
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.home.HomeViewModel
import com.example.myfooddelivery.ui.screens.home.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator


class SearchViewPagerFragment: Fragment() {

    private val binding: FragmentSearchViewPagerBinding by lazy {
        FragmentSearchViewPagerBinding.inflate(layoutInflater)
    }
    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(DatabaseProvider(requireActivity()))
    }
    private lateinit var viewPagerCallback: OnPageChangeCallback
    private var filterBottomSheet: BottomSheetDialog? = null
    private var sortBottomSheet: BottomSheetDialog? = null

    private fun setSearchBarEventListener() {
        val homeViewModel: HomeViewModel by viewModels(
            ownerProducer = {
                requireActivity().supportFragmentManager.findFragmentByTag(FragmentTags.HOME_FRAGMENT.name)!!
            }
        ) {
            HomeViewModelFactory(DatabaseProvider(requireContext()), LoggedInAccountDataStoreHelper(requireContext()))
        }
        searchViewModel.selectedCategory = homeViewModel.selectedCategory
        binding.searchBar.etSearchBar.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                binding.searchBar.btnSearchBar.visibility = View.VISIBLE
            else
                binding.searchBar.btnSearchBar.visibility = View.INVISIBLE
        }
        binding.searchBar.btnSearchBar.setOnClickListener {
            binding.searchBar.etSearchBar.clearFocus()
            binding.searchBar.etSearchBar.setText("")
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        binding.searchBar.etSearchBar.doOnTextChanged { text, _, _, _ ->
            searchViewModel.searchText.value = text.toString()
        }
        //searchViewModel.selectedCategory = ""
        if (searchViewModel.selectedCategory.isNotEmpty()) {
            searchViewModel.searchText.value = searchViewModel.selectedCategory
            binding.searchBar.etSearchBar.setText(searchViewModel.selectedCategory)
            binding.viewPager.setCurrentItem(ViewPagerMenus.FOOD_TAB.ordinal, false)
            searchViewModel.selectedCategory = ""
        }
    }

    private fun updateFilterOption(dialogBoxBinding: FilterDialogBinding) {
        if (dialogBoxBinding.rbVeg.isChecked) {
            searchViewModel.isVegFilterApplied.value = true
            binding.btnFilter.text = FilterType.VEG.sortFilterResourceWrapper.selectedName
            binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.VEG.sortFilterResourceWrapper.drawableId, 0, 0, 0)
        }
        else {
            searchViewModel.isVegFilterApplied.value = false
            binding.btnFilter.text = FilterType.NONE.sortFilterResourceWrapper.selectedName
            if (resources.configuration.isNightModeActive)
                binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            else
                binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
        }
    }

    private fun updateSortOptionHotel(tempBinding: ViewBinding, bottomSheetDialog: BottomSheetDialog) {
        val dialogBoxBinding = tempBinding as SortDialogHotelBinding
        dialogBoxBinding.btnApply.setOnClickListener {
            if (dialogBoxBinding.rbRating.isChecked) {
                searchViewModel.sortTypeHotel.value = SortType.RATINGS
                binding.btnSort.text = SortType.RATINGS.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.RATINGS.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else {
                binding.btnSort.text = SortType.NONE.sortFilterResourceWrapper.selectedName
                if (resources.configuration.isNightModeActive) {
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                    searchViewModel.sortTypeHotel.value = SortType.NONE_DARK_MODE
                }
                else {
                    searchViewModel.sortTypeHotel.value = SortType.NONE
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                }
            }
            bottomSheetDialog.hide()
        }
    }

    private fun updateSortOptionFood(tempBinding: ViewBinding, bottomSheetDialog: BottomSheetDialog) {
        val dialogBoxBinding = tempBinding as SortDialogFoodBinding
        dialogBoxBinding.btnApply.setOnClickListener {
            if (dialogBoxBinding.rbRating.isChecked) {
                searchViewModel.sortTypeFood.value = SortType.RATINGS
                binding.btnSort.text = SortType.RATINGS.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.RATINGS.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else if (dialogBoxBinding.rbPriceLowToHigh.isChecked) {
                searchViewModel.sortTypeFood.value = SortType.PRICE_LOW_TO_HIGH
                binding.btnSort.text = SortType.PRICE_LOW_TO_HIGH.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.PRICE_LOW_TO_HIGH.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else if (dialogBoxBinding.rbPriceHighToLow.isChecked) {
                searchViewModel.sortTypeFood.value = SortType.PRICE_HIGH_TO_LOW
                binding.btnSort.text = SortType.PRICE_HIGH_TO_LOW.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.PRICE_HIGH_TO_LOW.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else {
                binding.btnSort.text = SortType.NONE.sortFilterResourceWrapper.selectedName
                if (resources.configuration.isNightModeActive) {
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                    searchViewModel.sortTypeFood.value = SortType.NONE_DARK_MODE
                }
                else {
                    searchViewModel.sortTypeFood.value = SortType.NONE
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                }
            }
            bottomSheetDialog.hide()
        }
    }

    private fun setUpFilterBottomSheet() {
        binding.btnFilter.setOnClickListener {
            if (filterBottomSheet?.behavior?.state == BottomSheetBehavior.STATE_EXPANDED)
                filterBottomSheet?.hide()
            if (filterBottomSheet == null) {
                val style = if (context?.resources?.configuration?.isNightModeActive!!)
                    R.style.BaseBottomSheetDialogDark
                else
                    R.style.BaseBottomSheetDialogLight
                filterBottomSheet = BottomSheetDialog(requireContext(), style)
            }

            val bottomSheetBinding = FilterDialogBinding.inflate(layoutInflater)
            filterBottomSheet!!.setContentView(bottomSheetBinding.root)
            bottomSheetBinding.btnApply.setOnClickListener {
                updateFilterOption(bottomSheetBinding)
                filterBottomSheet!!.hide()
            }
            filterBottomSheet!!.dismissWithAnimation = true
            filterBottomSheet!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            filterBottomSheet!!.show()
        }
    }

    private fun setUpSortBottomSheet() {
        binding.btnSort.setOnClickListener {
            if (sortBottomSheet?.behavior?.state == BottomSheetBehavior.STATE_EXPANDED)
                sortBottomSheet?.hide()
            if (sortBottomSheet == null) {
                val style = if (context?.resources?.configuration?.isNightModeActive!!)
                    R.style.BaseBottomSheetDialogDark
                else
                    R.style.BaseBottomSheetDialogLight
                sortBottomSheet = BottomSheetDialog(requireContext(), style)
            }
            val tempBinding = if (searchViewModel.selectedOption == ViewPagerMenus.HOTEL_TAB)
                    SortDialogHotelBinding.inflate(layoutInflater)
                else
                    SortDialogFoodBinding.inflate(layoutInflater)
            sortBottomSheet!!.setContentView(tempBinding.root)
            if (searchViewModel.selectedOption == ViewPagerMenus.HOTEL_TAB)
                updateSortOptionHotel(tempBinding, sortBottomSheet!!)
            else
                updateSortOptionFood(tempBinding, sortBottomSheet!!)
            sortBottomSheet!!.dismissWithAnimation = true
            sortBottomSheet!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            sortBottomSheet!!.show()
        }
    }

    private fun initializeSelectedFilter() {
        if (searchViewModel.isVegFilterApplied.value!!) {
            binding.btnFilter.text = FilterType.VEG.sortFilterResourceWrapper.selectedName
            binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.VEG.sortFilterResourceWrapper.drawableId, 0, 0, 0)
        }
        else {
            binding.btnFilter.text = FilterType.NONE.sortFilterResourceWrapper.selectedName
            if (resources.configuration.isNightModeActive)
                binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            else
                binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
        }
    }

    private fun initializeSelectedSortForHotel() {
        when (searchViewModel.sortTypeHotel.value!!) {
            SortType.RATINGS -> {
                binding.btnSort.text = SortType.RATINGS.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.RATINGS.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else -> {
                binding.btnSort.text = SortType.NONE.sortFilterResourceWrapper.selectedName
                if (resources.configuration.isNightModeActive) {
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                    searchViewModel.sortTypeHotel.value = SortType.NONE_DARK_MODE
                }
                else {
                    searchViewModel.sortTypeHotel.value = SortType.NONE
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                }
            }
        }
    }

    private fun initializeSelectedSortForFood() {
        when (searchViewModel.sortTypeFood.value!!) {
            SortType.RATINGS -> {
                binding.btnSort.text = SortType.RATINGS.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.RATINGS.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            SortType.PRICE_HIGH_TO_LOW -> {
                binding.btnSort.text = SortType.PRICE_HIGH_TO_LOW.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.PRICE_HIGH_TO_LOW.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            SortType.PRICE_LOW_TO_HIGH -> {
                binding.btnSort.text = SortType.PRICE_LOW_TO_HIGH.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.PRICE_LOW_TO_HIGH.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else -> {
                binding.btnSort.text = SortType.NONE.sortFilterResourceWrapper.selectedName
                if (resources.configuration.isNightModeActive) {
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                    searchViewModel.sortTypeFood.value = SortType.NONE_DARK_MODE
                }
                else {
                    searchViewModel.sortTypeFood.value = SortType.NONE
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                }
            }
        }
    }

    private fun setUpSortAndFilterOptions() {
        setUpFilterBottomSheet()
        setUpSortBottomSheet()
        initializeSelectedFilter()
        initializeSelectedSortForHotel()
        initializeSelectedSortForFood()
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = ScreenSlidePagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        binding.viewPager.setCurrentItem(0, false)
        viewPagerCallback = object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == ViewPagerMenus.HOTEL_TAB.ordinal) {
                    searchViewModel.selectedOption = ViewPagerMenus.HOTEL_TAB
                    initializeSelectedSortForHotel()
                }
                else {
                    searchViewModel.selectedOption = ViewPagerMenus.FOOD_TAB
                    initializeSelectedSortForFood()
                }
            }

        }
        binding.viewPager.registerOnPageChangeCallback(viewPagerCallback)
    }

    private fun setBackButtonListener() {
        binding.btnBack.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.VISIBLE
            val activityViewModel: BaseActivityViewModel by activityViewModels()
            activityViewModel.selectedMenu = Screens.HOME_SCREEN
            parentFragmentManager.popBackStack()
        }
    }

    private fun hideBottomNavBar() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.GONE
    }

    private fun focusSearchBar() {
        binding.searchBar.etSearchBar.requestFocus()
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.showSoftInput(binding.searchBar.etSearchBar, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun updateNightModeChanges() {
        if (resources.configuration.isNightModeActive) {
            binding.btnBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.searchBar.btnSearchBar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.btnSort.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_dark)
            binding.btnFilter.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_dark)
        }
        else {
            binding.btnBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            binding.searchBar.btnSearchBar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnSort.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_light)
            binding.btnFilter.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_light)
        }
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

        hideBottomNavBar()
        updateNightModeChanges()
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.SEARCH_SCREEN
        setBackButtonListener()
        setUpSortAndFilterOptions()
        setUpViewPager()
        setSearchBarEventListener()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            if (position == ViewPagerMenus.HOTEL_TAB.ordinal)
                tab.text = activity?.resources?.getString(R.string.hotel_label)
            else
                tab.text = activity?.resources?.getString(R.string.food_label)
        }.attach()
        focusSearchBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.adapter = null  //if not put, fragment with id doesn't exist error
        binding.viewPager.unregisterOnPageChangeCallback(viewPagerCallback)
    }
}
package com.example.myfooddelivery.ui.screens.neworder

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.CartDataStoreHelper
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FilterDialogBinding
import com.example.myfooddelivery.databinding.FragmentNewOrderBinding
import com.example.myfooddelivery.databinding.MenuCategoriesBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionLandscapeBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionPortraitBinding
import com.example.myfooddelivery.databinding.SortDialogFoodBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FilterType
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.constants.FragmentTags
import com.example.myfooddelivery.constants.SortType
import com.example.myfooddelivery.constants.FoodType
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.helper.FavoriteAnimationHelper
import com.example.myfooddelivery.ui.helper.NewOrderAnimationHelper
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.ordersummary.OrderSummaryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


class NewOrderFragment : Fragment() {

    val viewModel: NewOrderViewModel by viewModels { NewOrderViewModelFactory(
        DatabaseProvider(requireContext()),
        CartDataStoreHelper(requireContext()),
        LoggedInAccountDataStoreHelper(requireContext())
    ) }
    private val binding: FragmentNewOrderBinding by lazy { FragmentNewOrderBinding.inflate(layoutInflater) }
    private var itemDescriptionBottomSheet: BottomSheetDialog? = null
    private var filterBottomSheet: BottomSheetDialog? = null
    private var sortBottomSheet: BottomSheetDialog? = null

    private suspend fun setUpHotelDetailsCard() {
        val hotelAccount = viewModel.getHotel()
        binding.tvHotelName.text = hotelAccount.name
        binding.ivHotelPicture.setImageResource(hotelAccount.imageId)
        binding.tvHotelDetails.text = hotelAccount.address.area
        viewModel.foodCategories = hotelAccount.categories
        setUpMenuButtonListener()
    }

    private suspend fun setUpLikeButtonListener() {
        val hotelAccount = viewModel.getHotel()
        val customerAccount = viewModel.getCustomerAccount()
        var isLiked = hotelAccount.hotelId in customerAccount?.favoriteHotels!!
        if (isLiked)
            binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
        else
            if (requireContext().resources.configuration.isNightModeActive)
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
            else
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24_dark)
        binding.btnFavorite.setOnClickListener {
            isLiked = !isLiked
            if (isLiked)
                binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_24)
            else
                if (requireContext().resources.configuration.isNightModeActive)
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                else
                    binding.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24_dark)
            FavoriteAnimationHelper.favoriteChangedAnimation(binding.btnFavorite)
            lifecycleScope.launch {
                viewModel.updateFavoriteHotels(hotelAccount.hotelId, isLiked)
            }
        }
    }

    private fun setCategoryClickListener(selectedCategory: String, alertDialog: AlertDialog) {
        val filteredCategory = viewModel.foodCategories.filter { selectedCategory == it.categoryWrapper.name }
        if (filteredCategory.isNotEmpty())
            viewModel.selectedCategory = filteredCategory
        else
            viewModel.selectedCategory = viewModel.foodCategories
        lifecycleScope.launch {
            updateRecyclerViewAdapter()
            delay(250)
            alertDialog.hide()
            if (binding.btnFilter.visibility == View.GONE) {//to make back button invisible
                binding.btnBackNestedScrollView.visibility = View.GONE
            }
        }
    }

    private fun setUpMenuButtonListener() {
        if (requireActivity().resources.configuration.isNightModeActive) {
            binding.btnMenu.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
        else {
            binding.btnMenu.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        var isMenuShown = false
        binding.btnMenu.setOnClickListener {
            val style = if (this.resources.configuration.isNightModeActive)
                R.style.categoriesAlertDialogDark
            else
                R.style.categoriesAlertDialogLight
            val listItems = viewModel.foodCategories.map { it.categoryWrapper.name }.let {
                it.toMutableList().apply {
                    add(0, resources.getString(R.string.all_items_text))
                }
            }
            val dialogBoxBinding = MenuCategoriesBinding.inflate(layoutInflater)
            //dialogBoxBinding.lvCategories.choiceMode = ListView.CHOICE_MODE_SINGLE
            dialogBoxBinding.lvCategories.adapter = ArrayAdapter(requireContext(),
                R.layout.category_list_item,
                listItems
            )
            val dialog = AlertDialog.Builder(requireContext(), style)
                .setView(dialogBoxBinding.root)
                .create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.setOnCancelListener {
                dialog.hide()
                isMenuShown = false
            }
            dialogBoxBinding.lvCategories.setOnItemClickListener { adapterView, _, position, _ ->
                setCategoryClickListener(adapterView.getItemAtPosition(position) as String, dialog)

                isMenuShown = false
            }
            if (!isMenuShown) {
                isMenuShown = true
                dialog.show()
            }
        }
    }

    private fun setUpItemDetailsBottomSheetInPortraitMode(bottomSheetDialog: BottomSheetDialog, item: Item) {
        val bottomSheetBinding = MenuItemDescriptionPortraitBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.ivItem.setImageResource(item.imageId)
        if(item.isVeg){
            bottomSheetBinding.ivItemType.setImageResource(R.drawable.veg_indicator)
            bottomSheetBinding.tvItemType.text = FoodType.VEG.value
            bottomSheetBinding.tvItemType.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        else{
            bottomSheetBinding.ivItemType.setImageResource(R.drawable.non_veg_indicator)
            bottomSheetBinding.tvItemType.text = FoodType.NON_VEG.value
            bottomSheetBinding.tvItemType.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_color))
        }
        bottomSheetBinding.tvItemTitle.text = item.itemName
        bottomSheetBinding.tvItemPrice.text = resources.getString(R.string.item_price, item.itemPrice)
        bottomSheetBinding.tvItemDescription.text = item.description
    }

    private fun setUpItemDetailsBottomSheetInLandscapeMode(bottomSheetDialog: BottomSheetDialog, item: Item) {
        val bottomSheetBinding = MenuItemDescriptionLandscapeBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.ivItem.setImageResource(item.imageId)
        if(item.isVeg){
            bottomSheetBinding.ivItemType.setImageResource(R.drawable.veg_indicator)
            bottomSheetBinding.tvItemType.text = FoodType.VEG.value
            bottomSheetBinding.tvItemType.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        else{
            bottomSheetBinding.ivItemType.setImageResource(R.drawable.non_veg_indicator)
            bottomSheetBinding.tvItemType.text = FoodType.NON_VEG.value
            bottomSheetBinding.tvItemType.setTextColor(ContextCompat.getColor(requireContext(), R.color.app_color))
        }
        bottomSheetBinding.tvItemTitle.text = item.itemName
        bottomSheetBinding.tvItemPrice.text = resources.getString(R.string.item_price, item.itemPrice)
        bottomSheetBinding.tvItemDescription.text = item.description
    }

    private fun showItemDetailsBottomSheet(item: Item) {
        if (itemDescriptionBottomSheet?.behavior?.state == BottomSheetBehavior.STATE_EXPANDED)
            itemDescriptionBottomSheet?.hide()
        if (itemDescriptionBottomSheet == null) {
            val style = if (context?.resources?.configuration?.isNightModeActive!!)
                R.style.BaseBottomSheetDialogDark
            else
                R.style.BaseBottomSheetDialogLight
            itemDescriptionBottomSheet = BottomSheetDialog(requireContext(), style)
        }
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            setUpItemDetailsBottomSheetInPortraitMode(itemDescriptionBottomSheet!!, item)
        else
            setUpItemDetailsBottomSheetInLandscapeMode(itemDescriptionBottomSheet!!, item)
        itemDescriptionBottomSheet!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        itemDescriptionBottomSheet!!.show()
    }

    private suspend fun updateRecyclerViewAdapter() {
        binding.rvOrderItemList.adapter = NewOrderAdapter(viewModel.getFilteredItems(), viewModel, binding, requireContext()).apply {
            lifecycleScope.launch {
                if (items.isEmpty()) {
                    binding.tvEmptyResult.visibility = View.VISIBLE
                    binding.ivEmptyResult.visibility = View.VISIBLE
                }
                else {
                    binding.tvEmptyResult.visibility = View.INVISIBLE
                    binding.ivEmptyResult.visibility = View.INVISIBLE
                }
            }
            setOnClickListener(object: NewOrderAdapter.OnClickListener {
                override fun onClick(position: Int, item: Item) {
                    showItemDetailsBottomSheet(item)
                }
            })
        }

    }

    private suspend fun setUpRecyclerView() {
        binding.rvOrderItemList.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            updateRecyclerViewAdapter()
        }
        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val params = binding.rvOrderItemList.layoutParams
            params.height = (resources.displayMetrics.widthPixels * 0.43).toInt()
            binding.rvOrderItemList.layoutParams = params
            var isFilterOptionsVisible = true
            binding.rvOrderItemList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0 && isFilterOptionsVisible) {
                        isFilterOptionsVisible = false
                        NewOrderAnimationHelper.hideFilterOptions(binding)
                    } else if (dy < 0 && !isFilterOptionsVisible) {
                        isFilterOptionsVisible = true
                        NewOrderAnimationHelper.showFilterOptions(binding)
                    }
                }
            })
        }
    }

    private fun setSearchBarEventListener() {
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
            lifecycleScope.launch {
                viewModel.searchText = text.toString()
                updateRecyclerViewAdapter()
            }
        }
        if (binding.searchBar.etSearchBar.hasFocus())
            binding.searchBar.btnSearchBar.visibility = View.VISIBLE
        else
            binding.searchBar.btnSearchBar.visibility = View.INVISIBLE
    }

    private fun setViewCartBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.viewCardBottomSheet.root)
        binding.viewCardBottomSheet.root.setOnClickListener {  }
        bottomSheetBehavior.isDraggable = false
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clBottom)
        if (viewModel.totalPrice.value!! > 0) {
            binding.viewCardBottomSheet.btnPlaceOrder.text = resources.getString(R.string.view_cart_with_price, viewModel.totalPrice.value)
            binding.viewCardBottomSheet.tvTotalItemsSelected.text = resources.getString(R.string.total_items_added, viewModel.totalItemsSelected)
            constraintSet.connect(binding.btnMenu.id, ConstraintSet.BOTTOM, binding.coordinatorLayout.id, ConstraintSet.TOP)
            constraintSet.applyTo(binding.clBottom)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            constraintSet.connect(binding.btnMenu.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            constraintSet.applyTo(binding.clBottom)
        }
        viewModel.totalPrice.observe(viewLifecycleOwner) {
            if (it!! > 0) {
                constraintSet.connect(binding.btnMenu.id, ConstraintSet.BOTTOM, binding.coordinatorLayout.id, ConstraintSet.TOP)
                TransitionManager.beginDelayedTransition(binding.clBottom)
                constraintSet.applyTo(binding.clBottom)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                if (resources.configuration.isNightModeActive)
                    binding.viewCardBottomSheet.ivCart.setImageResource(R.drawable.cart_dark_mode)
                else
                    binding.viewCardBottomSheet.ivCart.setImageResource(R.drawable.cart)
                binding.viewCardBottomSheet.btnPlaceOrder.isClickable = true
                binding.viewCardBottomSheet.btnPlaceOrder.text = resources.getString(R.string.view_cart_with_price, viewModel.totalPrice.value)
                binding.viewCardBottomSheet.tvTotalItemsSelected.text = resources.getString(R.string.total_items_added, viewModel.totalItemsSelected)
            }
            else {
                binding.viewCardBottomSheet.btnPlaceOrder.isClickable = false
                binding.viewCardBottomSheet.btnPlaceOrder.text = resources.getString(R.string.view_cart_text)
                if (viewModel.totalItemsSelected == 0) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//                    val layoutParams = ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
//                    layoutParams.bottomMargin = 0
                    constraintSet.connect(binding.btnMenu.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                    TransitionManager.beginDelayedTransition(binding.clBottom)
                    constraintSet.applyTo(binding.clBottom)
                }
            }
            lifecycleScope.launch {
                delay(200) //give delay to avoid bug when adding and removing at same time
                if (viewModel.itemsWithQuantity.size == 1) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        binding.viewCardBottomSheet.btnPlaceOrder.setOnClickListener {
            parentFragmentManager.commit {
                val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
                val activityViewModel: BaseActivityViewModel by activityViewModels()
                activityViewModel.selectedMenu = Screens.ORDER_SUMMARY_SCREEN
                replace(activityBaseBinding.flBaseFragment.id, OrderSummaryFragment())
                addToBackStack(null)
            }
        }
    }

    private fun updateFilterOption(dialogBoxBinding: FilterDialogBinding) {
        if (dialogBoxBinding.rbVeg.isChecked) {
            viewModel.isVegFilterApplied = true
            binding.btnFilter.text = FilterType.VEG.sortFilterResourceWrapper.selectedName
            binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.VEG.sortFilterResourceWrapper.drawableId, 0, 0, 0)
        }
        else {
            viewModel.isVegFilterApplied = false
            binding.btnFilter.text = FilterType.NONE.sortFilterResourceWrapper.selectedName
            if (resources.configuration.isNightModeActive)
                binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            else
                binding.btnFilter.setCompoundDrawablesWithIntrinsicBounds(FilterType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
        }
        lifecycleScope.launch {
            updateRecyclerViewAdapter()
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

    private fun updateSortOption(dialogBoxBinding: SortDialogFoodBinding, bottomSheet: BottomSheetDialog) {
        dialogBoxBinding.btnApply.setOnClickListener {
            if (dialogBoxBinding.rbRating.isChecked) {
                viewModel.sortType = SortType.RATINGS
                binding.btnSort.text = SortType.RATINGS.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.RATINGS.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else if (dialogBoxBinding.rbPriceLowToHigh.isChecked) {
                viewModel.sortType = SortType.PRICE_LOW_TO_HIGH
                binding.btnSort.text = SortType.PRICE_LOW_TO_HIGH.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.PRICE_LOW_TO_HIGH.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else if (dialogBoxBinding.rbPriceHighToLow.isChecked) {
                viewModel.sortType = SortType.PRICE_HIGH_TO_LOW
                binding.btnSort.text = SortType.PRICE_HIGH_TO_LOW.sortFilterResourceWrapper.selectedName
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.PRICE_HIGH_TO_LOW.sortFilterResourceWrapper.drawableId, 0, 0, 0)
            }
            else {
                binding.btnSort.text = SortType.NONE.sortFilterResourceWrapper.selectedName
                if (resources.configuration.isNightModeActive) {
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE_DARK_MODE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                    viewModel.sortType = SortType.NONE_DARK_MODE
                }
                else {
                    viewModel.sortType = SortType.NONE
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                }
            }
            bottomSheet.hide()
            lifecycleScope.launch {
                updateRecyclerViewAdapter()
            }
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
            val tempBinding = SortDialogFoodBinding.inflate(layoutInflater)
            sortBottomSheet!!.setContentView(tempBinding.root)
                updateSortOption(tempBinding, sortBottomSheet!!)
            sortBottomSheet!!.dismissWithAnimation = true
            sortBottomSheet!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            sortBottomSheet!!.show()
        }
    }

    private fun initializeSelectedFilter() {
        if (viewModel.isVegFilterApplied) {
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

    private fun initializeSelectedSort() {
        when (viewModel.sortType) {
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
                    viewModel.sortType = SortType.NONE_DARK_MODE
                }
                else {
                    viewModel.sortType = SortType.NONE
                    binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(SortType.NONE.sortFilterResourceWrapper.drawableId, 0, 0, 0)
                }
            }
        }
    }

    private fun goBack() {
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        if (parentFragmentManager.backStackEntryCount > 1)
            activityViewModel.selectedMenu = Screens.SEARCH_SCREEN
        else {
            if (parentFragmentManager.findFragmentByTag(FragmentTags.HOME_FRAGMENT.name) != null)
                activityViewModel.selectedMenu = Screens.HOME_SCREEN
            else
                activityViewModel.selectedMenu = Screens.FAVORITES_SCREEN
        }
        parentFragmentManager.popBackStack()
        if (activityViewModel.selectedMenu != Screens.SEARCH_SCREEN)
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav_view).visibility = View.VISIBLE
    }

    private fun setBackButtonListener() {
        binding.btnBackCollapsingToolbar.setOnClickListener {
            goBack()
        }
        binding.btnBackNestedScrollView.setOnClickListener {
            goBack()
        }
    }

    private fun setCollapsingToolbarListener() {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                NewOrderAnimationHelper.showBackButton(binding)
            }
            else {
                NewOrderAnimationHelper.hideBackButton(binding)
            }
        }
    }

    private fun setUpSortAndFilterOptions() {
        setUpFilterBottomSheet()
        setUpSortBottomSheet()
        initializeSelectedFilter()
        initializeSelectedSort()
    }


    private fun updateNightModeChanges() {
        if (context?.resources?.configuration?.isNightModeActive!!) {
            binding.clCustomerOrder.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnBackCollapsingToolbar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.btnBackNestedScrollView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.searchBar.btnSearchBar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.btnSort.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_dark)
            binding.btnFilter.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_dark)
        }
        else {
            binding.clCustomerOrder.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            binding.btnBackCollapsingToolbar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnBackNestedScrollView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            binding.searchBar.btnSearchBar.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnSort.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_light)
            binding.btnFilter.background = ContextCompat.getDrawable(requireContext(), R.drawable.spinner_background_light)
        }
    }

    private fun initializeViews() {
        lifecycleScope.launch {
            viewModel.checkIsCartPresent()
            setViewCartBottomSheet()
            setUpHotelDetailsCard()
            setUpRecyclerView()
            setSearchBarEventListener()
            setUpSortAndFilterOptions()
            setUpLikeButtonListener()
        }
        setCollapsingToolbarListener()
        setBackButtonListener()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.appBarLayout.setExpanded(true)
            NewOrderAnimationHelper.showFilterOptions(binding)
        }
    }

    private fun hideBottomNavBar() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.GONE
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

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.viewCardBottomSheet.root)
        //val constraintSet = ConstraintSet()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        constraintSet.connect(binding.btnMenu.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
//        constraintSet.applyTo(binding.clBottom)
        hideBottomNavBar()
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.NEW_ORDER_SCREEN
        updateNightModeChanges()
        if(viewModel.hotelId == -1) {
            activity?.supportFragmentManager!!.setFragmentResultListener(FragmentResultConstants.FRAGMENT_RESULT_KEY.name, viewLifecycleOwner) { _, bundle ->
                viewModel.hotelId = bundle.getInt(FragmentResultConstants.HOTEL_ID_KEY.name, -2)
                //calling setUpRecyclerView() outside of listener causes viewModel hotelId to initialize late and gives wrong results
                initializeViews()
            }
        }
        else {
            initializeViews()
        }
    }
}
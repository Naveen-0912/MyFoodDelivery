package com.example.myfooddelivery.ui.screens.particularorder

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FragmentViewParticularOrderBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionLandscapeBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionPortraitBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.constants.FoodType
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.myorders.ViewOrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class ViewParticularOrderFragment: Fragment() {
    private val viewModel: ViewParticularOrderViewModel by viewModels {
        ViewParticularOrderViewModelFactory(
            DatabaseProvider(requireContext())
        )
    }
    private val binding: FragmentViewParticularOrderBinding by lazy {
        FragmentViewParticularOrderBinding.inflate(layoutInflater)
    }
    private val foodItemsAdapter: ViewParticularOrderAdapter by lazy { ViewParticularOrderAdapter(listOf(), requireContext()) }
    private var bottomSheetDialog: BottomSheetDialog? = null

    private fun setUpRecyclerView() {
        foodItemsAdapter.setOnClickListener(object: ViewParticularOrderAdapter.OnClickListener {
            override fun onClick(item: Item) {
                showItemDetailsBottomSheet(item)
            }
        })
        binding.rvFoodItems.adapter =  foodItemsAdapter
        binding.rvFoodItems.layoutManager = LinearLayoutManager(activity?.baseContext)
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
        bottomSheetDialog.let {
            if (bottomSheetDialog?.behavior?.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetDialog?.hide()
            if (bottomSheetDialog == null) {
                val style = if (context?.resources?.configuration?.isNightModeActive!!)
                    R.style.BaseBottomSheetDialogDark
                else
                    R.style.BaseBottomSheetDialogLight
                bottomSheetDialog = BottomSheetDialog(requireContext(), style)
            }
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                setUpItemDetailsBottomSheetInPortraitMode(bottomSheetDialog!!, item)
            else
                setUpItemDetailsBottomSheetInLandscapeMode(bottomSheetDialog!!, item)
            bottomSheetDialog!!.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog!!.show()
        }
    }

    private fun updateViews() {
        if (viewModel.orderedItemsWithQuantityWrapper != null) {
            foodItemsAdapter.itemsWithQuantityWrapper = viewModel.orderedItemsWithQuantityWrapper!!
            foodItemsAdapter.notifyDataSetChanged()
            val totalPrice = foodItemsAdapter.itemsWithQuantityWrapper.sumOf { it.quantity * it.item.itemPrice }
            binding.btnTotalPrice.text = resources.getString(R.string.ordered_items_total_price, totalPrice)
        }
    }

    private fun setBackButtonListener() {
        binding.btnBack.setOnClickListener {
            val activityBinding = ActivityBaseBinding.inflate(LayoutInflater.from(requireContext()))
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(activityBinding.flBaseFragment.id, ViewOrdersFragment())
            }
        }
    }

    private fun updateNightModeChanges() {
        if (resources.configuration.isNightModeActive)
            binding.btnBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
        else
            binding.btnBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
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

        hideBottomNavBar()
        updateNightModeChanges()
        setBackButtonListener()
        setUpRecyclerView()
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.VIEW_PARTICULAR_ORDER_SCREEN
        setFragmentResultListener(FragmentResultConstants.FRAGMENT_RESULT_KEY.name) { _, bundle ->
            viewModel.orderId = bundle.getInt(FragmentResultConstants.ORDER_ID_KEY.name, viewModel.orderId)
            lifecycleScope.launch {
                if(viewModel.initializeOrderWithOrderId())
                    updateViews()
            }
        }
        if (viewModel.orderId > -1) {
            lifecycleScope.launch {
                if(viewModel.initializeOrderWithOrderId())
                    updateViews()
            }
        }
    }
}
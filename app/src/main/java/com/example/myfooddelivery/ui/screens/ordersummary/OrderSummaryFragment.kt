package com.example.myfooddelivery.ui.screens.ordersummary

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.CartDataStoreHelper
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.service.OrderDispatchedService
import com.example.myfooddelivery.databinding.FragmentOrderSummaryBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionLandscapeBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionPortraitBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FoodType
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class OrderSummaryFragment : Fragment() {

    private val viewModel: OrderSummaryViewModel by viewModels {
        OrderSummaryViewModelFactory(
            DatabaseProvider(requireContext()),
            LoggedInAccountDataStoreHelper(requireContext()),
            CartDataStoreHelper(requireContext())
        )
    }

    private val binding: FragmentOrderSummaryBinding by lazy { FragmentOrderSummaryBinding.inflate(layoutInflater) }
    private val orderSummaryAdapter: OrderSummaryAdapter by lazy { OrderSummaryAdapter(mutableListOf(), viewModel, requireContext()) }
    private var bottomSheetDialog: BottomSheetDialog? =null

    private var myService: OrderDispatchedService? = null
    //var isBound = false

    private val myConnection: ServiceConnection by lazy { object :ServiceConnection {
        override fun onServiceConnected(className: ComponentName,
                                        service: IBinder
        ) {
            val binder = service as OrderDispatchedService.MyBinder
            myService = binder.getService()
            myService?.addOrder(viewModel.orderId)
            context?.unbindService(this)
            //isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            myService = null
        }
    } }

    private fun setUpHotelDetailsCard() {
        binding.tvHotelName.text = viewModel.hotelName
    }

    private fun setUpRecyclerView() {
        binding.rvOrderItemList.adapter =  orderSummaryAdapter
        binding.rvOrderItemList.isNestedScrollingEnabled = false
        binding.rvOrderItemList.layoutManager = LinearLayoutManager(requireContext())
        orderSummaryAdapter.setOnClickListener(object: OrderSummaryAdapter.OnClickListener {
            override fun onClick(position: Int, item: Item) {
                showItemDetailsBottomSheet(item)
            }
        })
        orderSummaryAdapter.itemsWithQuantityWrapper = viewModel.itemWithQuantityWrapperList
        orderSummaryAdapter.notifyDataSetChanged()
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

    private fun setPlaceOrderButtonListener() {
        if (viewModel.totalPrice.value!! > 0)
            binding.btnPlaceOrder.text = resources.getString(R.string.place_order_with_price, viewModel.totalPrice.value)
        binding.btnPlaceOrder.setOnClickListener {
            lifecycleScope.launch {
                viewModel.orderId = viewModel.placeOrder().toInt()
                if(viewModel.orderId > -1) {
                    Toast.makeText(activity, "Order Placed", Toast.LENGTH_SHORT).show()
                    requireContext().applicationContext.startForegroundService(Intent(requireContext().applicationContext, OrderDispatchedService::class.java))
                    context?.bindService(Intent(requireContext().applicationContext, OrderDispatchedService::class.java), myConnection, Context.BIND_AUTO_CREATE)
                    parentFragmentManager.popBackStack()
                    parentFragmentManager.popBackStack()
                }
                else
                    Toast.makeText(activity, "Failed to place order", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpBillAndCustomerDetails() {
        lifecycleScope.launch {
            binding.tvTotalAmount.text = resources.getString(R.string.to_pay_with_price, viewModel.totalPrice.value)
            val customerAccount = viewModel.customerAccount.await()
            if (activity?.resources?.configuration?.isNightModeActive!!) {
                binding.ivNameIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.pale_white))
                binding.ivMobileIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.pale_white))
                binding.ivAddressIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.pale_white))
            }
            else {
                binding.ivNameIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.black_light))
                binding.ivMobileIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.black_light))
                binding.ivAddressIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.black_light))
            }
            binding.tvCustomerName.text = customerAccount.name
            binding.tvCustomerMobile.text = customerAccount.mobileNo
            binding.tvCustomerAddress.text = resources.getString(
                R.string.customer_address,
                customerAccount.address.no,
                customerAccount.address.street,
                customerAccount.address.area,
                customerAccount.address.state,
                customerAccount.address.pinCode
            )
        }
        viewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalAmount.text = resources.getString(R.string.to_pay_with_price, it)
            binding.btnPlaceOrder.text = resources.getString(R.string.place_order_with_price, it)
        }
    }

    private fun setOrderedItemListListener() {
        viewModel.isOrderedItemsEmpty.observe(viewLifecycleOwner) {
            val activityViewModel: BaseActivityViewModel by activityViewModels()
            activityViewModel.selectedMenu = Screens.NEW_ORDER_SCREEN
            if (it) parentFragmentManager.popBackStack()
        }
    }

    private fun setBackButtonListener() {
        binding.btnBack.setOnClickListener {
            val activityViewModel: BaseActivityViewModel by activityViewModels()
            activityViewModel.selectedMenu = Screens.NEW_ORDER_SCREEN
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateNightModeChanges() {
        if (context?.resources?.configuration?.isNightModeActive!!) {
            binding.tvHotelName.setTextColor(ContextCompat.getColor(requireContext(), R.color.pale_white))
            binding.btnBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.pale_white))
        }
        else {
            binding.tvHotelName.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.btnBack.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
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

        hideBottomNavBar()
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.ORDER_SUMMARY_SCREEN
        lifecycleScope.launch {
            viewModel.initializeCartItems()
            setUpHotelDetailsCard()
            setUpRecyclerView()
            setUpBillAndCustomerDetails()
            setPlaceOrderButtonListener()
        }
        updateNightModeChanges()
        setBackButtonListener()
        setOrderedItemListListener()
    }
}
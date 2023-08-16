package com.example.myfooddelivery.ui.screens.orderdispatched

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.data.service.OrderDispatchedService
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FragmentOrderDispatchedBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionLandscapeBinding
import com.example.myfooddelivery.databinding.MenuItemDescriptionPortraitBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.constants.OrderCancelConstants
import com.example.myfooddelivery.constants.FoodType
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.myorders.ViewOrdersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class OrderDispatchedFragment : Fragment() {

    private val binding: FragmentOrderDispatchedBinding by lazy { FragmentOrderDispatchedBinding.inflate(layoutInflater) }
    private val orderDispatchedAdapter: OrderDispatchedAdapter by lazy { OrderDispatchedAdapter(listOf(), requireContext()) }
    private var bottomSheetDialog: BottomSheetDialog? = null
    private val viewModel: OrderDispatchedViewModel by viewModels { OrderDispatchedViewModelFactory(
        DatabaseProvider(requireContext()),
        LoggedInAccountDataStoreHelper(requireContext())
    )}
    private val observer = Observer<Int> {
        val minutes = (OrderCancelConstants.MAX_SECONDS.value - it) / OrderCancelConstants.SECONDS.value
        var seconds = OrderCancelConstants.SECONDS.value - (it % OrderCancelConstants.SECONDS.value)
        binding.progressBar.setProgressCompat(it, true)
        if (seconds == OrderCancelConstants.SECONDS.value)
            seconds = 0
        binding.tvProgressBar.text = requireContext().resources.getString(R.string.order_cancel_timer, minutes, if(seconds < 10) "0" else "", seconds)
        if (it == OrderCancelConstants.MAX_SECONDS.value) {
            val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(activityBaseBinding.flBaseFragment.id, ViewOrdersFragment())
            }
        }
    }
    private var myService: OrderDispatchedService? = null
    var isBound = false

    private val myConnection: ServiceConnection by lazy {
        object: ServiceConnection {
            override fun onServiceConnected(className: ComponentName,
                                            service: IBinder
            ) {
                val binder = service as OrderDispatchedService.MyBinder
                myService = binder.getService()
                if (!myService?.orderWithTimer?.containsKey(viewModel.orderId)!!)
                    lifecycleScope.launch {
                        myService?.deliverOrder(viewModel.orderId)
                        val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
                        parentFragmentManager.commit {
                            setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                            replace(activityBaseBinding.flBaseFragment.id, ViewOrdersFragment())
                        }
                    }
                setUpCancelOrderTimer()
                isBound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                myService = null
            }
        }
    }

    private fun setUpCancelOrderTimer() {
        myService?.orderWithTimer?.get(viewModel.orderId)?.observe(viewLifecycleOwner, observer)
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

    private fun setUpOrderSummaryRecyclerView() {
        orderDispatchedAdapter.setOnClickListener(object: OrderDispatchedAdapter.OnClickListener {
            override fun onClick(item: Item) {
                showItemDetailsBottomSheet(item)
            }
        })
        binding.rvOrderItemList.adapter =  orderDispatchedAdapter
        binding.rvOrderItemList.layoutManager = LinearLayoutManager(activity?.baseContext)
    }

    private fun setOrderCancelButtonListener() {
        binding.btnCancelOrder.setOnClickListener {
            lifecycleScope.launch {
                viewModel.cancelOrder()
                if (isBound) {
                    if (myService?.orderWithTimer?.containsKey(viewModel.orderId)!!) {
                        myService?.removeOrder(viewModel.orderId)
                        isBound = false
                        requireContext().unbindService(myConnection)
                    }
                }
                val activityViewModel: BaseActivityViewModel by activityViewModels()
                activityViewModel.selectedMenu = Screens.ORDER_SCREEN
                val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
                parentFragmentManager.commit {
                    setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                    replace(activityBaseBinding.flBaseFragment.id, ViewOrdersFragment())
                }
                activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpBillAndCustomerDetails() {
        lifecycleScope.launch {
            binding.tvTotalAmount.text = resources.getString(R.string.to_pay_with_price, viewModel.totalPrice)
            val customerAccount = viewModel.customerAccount.await()
            if (activity?.resources?.configuration?.isNightModeActive!!)
                binding.ivAddressIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.pale_white))
            else
                binding.ivAddressIcon.setColorFilter(ContextCompat.getColor(activity?.baseContext!!, R.color.black_light))
            binding.tvCustomerAddress.text = resources.getString(
                R.string.customer_address,
                customerAccount.address.no,
                customerAccount.address.street,
                customerAccount.address.area,
                customerAccount.address.state,
                customerAccount.address.pinCode
            )
        }
            binding.tvTotalAmount.text = resources.getString(R.string.to_pay_with_price, viewModel.totalPrice)
    }

    private fun updateViews() {
        if (viewModel.orderedItemsWithQuantityWrapper != null)
            orderDispatchedAdapter.itemsWithQuantityWrapper = viewModel.orderedItemsWithQuantityWrapper!!
        orderDispatchedAdapter.notifyDataSetChanged()
        binding.tvHotelName.text = viewModel.hotelName
        setUpBillAndCustomerDetails()
    }

    private fun setBackButtonListener() {
        binding.btnBack.setOnClickListener {
            val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
                replace(activityBaseBinding.flBaseFragment.id, ViewOrdersFragment())
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

    override fun onResume() {
        super.onResume()
        requireContext().bindService(Intent(requireContext().applicationContext, OrderDispatchedService::class.java), myConnection, Context.BIND_AUTO_CREATE)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideBottomNavBar()
        updateNightModeChanges()
        setBackButtonListener()
        setUpOrderSummaryRecyclerView()
        setOrderCancelButtonListener()
        val activityViewModel: BaseActivityViewModel by activityViewModels()
        activityViewModel.selectedMenu = Screens.ORDER_DISPATCHED_SCREEN
        setFragmentResultListener(
            FragmentResultConstants.FRAGMENT_RESULT_KEY.name
        ) { _, bundle ->
            viewModel.orderId = bundle.getInt(FragmentResultConstants.ORDER_ID_KEY.name, viewModel.orderId)
            lifecycleScope.launch {
                viewModel.initializeOrderWithOrderId()
                updateViews()
            }
        }
        if (viewModel.orderId > -1) {
            lifecycleScope.launch {
                viewModel.initializeOrderWithOrderId()
                updateViews()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        myService?.orderWithTimer?.get(viewModel.orderId)?.removeObserver(observer)
        if (isBound) {
            isBound = false
            requireContext().unbindService(myConnection)
        }
    }
}
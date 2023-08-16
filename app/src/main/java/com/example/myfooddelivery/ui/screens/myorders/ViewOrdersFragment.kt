package com.example.myfooddelivery.ui.screens.myorders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.Order
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FragmentViewOrdersBinding
import com.example.myfooddelivery.constants.Screens
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.BaseActivityViewModel
import com.example.myfooddelivery.ui.screens.orderdispatched.OrderDispatchedFragment
import com.example.myfooddelivery.ui.screens.particularorder.ViewParticularOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ViewOrdersFragment: Fragment() {

    private val binding: FragmentViewOrdersBinding by lazy { FragmentViewOrdersBinding.inflate(layoutInflater) }
    private val viewModel: ViewOrdersViewModel by viewModels { ViewOrdersViewModelFactory(
        DatabaseProvider(activity?.baseContext!!),
        LoggedInAccountDataStoreHelper((activity?.baseContext!!))
    ) }
    private val deliveredOrdersAdapter: DeliveredOrdersAdapter by lazy { DeliveredOrdersAdapter(listOf(), viewModel, requireContext()) }
    private val activeOrdersAdapter: ActiveCancelledOrdersAdapter by lazy { ActiveCancelledOrdersAdapter(listOf(), viewModel, requireContext()) }
    private val cancelledOrdersAdapter: ActiveCancelledOrdersAdapter by lazy { ActiveCancelledOrdersAdapter(listOf(), viewModel, requireContext()) }

    private fun redirectToViewParticularOrderFragment(orderId: Int) {
        val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
        parentFragmentManager.commit {
            setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
            setFragmentResult(
                FragmentResultConstants.FRAGMENT_RESULT_KEY.name,
                bundleOf(FragmentResultConstants.ORDER_ID_KEY.name to orderId)
            )
            add(activityBaseBinding.flBaseFragment.id, ViewParticularOrderFragment())
        }
    }

    private fun redirectToViewOrderDispatchedFragment(orderId: Int) {
        val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
        parentFragmentManager.commit {
            setCustomAnimations(R.anim.pop_fade_in, R.anim.pop_fade_out)
            setFragmentResult(
                FragmentResultConstants.FRAGMENT_RESULT_KEY.name,
                bundleOf(FragmentResultConstants.ORDER_ID_KEY.name to orderId)
            )
            replace(activityBaseBinding.flBaseFragment.id, OrderDispatchedFragment())
        }
    }

    private fun setUpDeliveredOrdersAdapter() {
        lifecycleScope.launch {
            deliveredOrdersAdapter.orders = viewModel.getDeliveredOrders()
            deliveredOrdersAdapter.notifyDataSetChanged()
            if (deliveredOrdersAdapter.orders.isEmpty()) {
                binding.tvDeliveredOrders.visibility = View.GONE
                binding.rvDeliveredOrders.visibility = View.GONE
            }
            else {
                binding.tvDeliveredOrders.visibility = View.VISIBLE
                binding.rvDeliveredOrders.visibility = View.VISIBLE
            }
            if (activeOrdersAdapter.orders.isEmpty() && cancelledOrdersAdapter.orders.isEmpty() && deliveredOrdersAdapter.orders.isEmpty()) {  //called here because at this point everything is initialized
                binding.ivEmptyOrder.visibility = View.VISIBLE
                binding.tvEmptyOrder.visibility = View.VISIBLE
            }
            else {
                binding.ivEmptyOrder.visibility = View.INVISIBLE
                binding.tvEmptyOrder.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpDeliveredOrdersRecyclerView() {
        binding.rvDeliveredOrders.layoutManager = LinearLayoutManager(requireContext())
        (binding.rvDeliveredOrders.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        deliveredOrdersAdapter.setHasStableIds(true)
        binding.rvDeliveredOrders.adapter =  deliveredOrdersAdapter
        setUpDeliveredOrdersAdapter()
        deliveredOrdersAdapter.setOnLikeClickListener(object: DeliveredOrdersAdapter.OnLikeClickListener {
            override fun onClick(hotelId: Int, isLiked: Boolean, sameHotelsPosition: List<Int>) {
                lifecycleScope.launch {
                    delay(300)
                    viewModel.updateFavoriteHotels(hotelId, isLiked)
                    sameHotelsPosition.forEach {
                        deliveredOrdersAdapter.notifyItemChanged(it)
                    }
                }
            }
        })
        deliveredOrdersAdapter.setOnClickListener(object: DeliveredOrdersAdapter.OnClickListener {
            override fun onClick(position: Int, order: Order) {
                redirectToViewParticularOrderFragment(order.orderId)
            }
        })
    }

    private fun setUpCancelledOrdersRecyclerView() {
        binding.rvCancelledOrders.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launch {
                cancelledOrdersAdapter.orders = viewModel.getCancelledOrders()
                cancelledOrdersAdapter.notifyDataSetChanged()
                if (cancelledOrdersAdapter.orders.isEmpty()) {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.clViewOrders)
                    constraintSet.setVisibility(binding.tvCancelledOrders.id, View.GONE)
                    constraintSet.setVisibility(binding.rvCancelledOrders.id, View.GONE)
                    constraintSet.connect(binding.tvDeliveredOrders.id, ConstraintSet.TOP, binding.rvActiveOrders.id, ConstraintSet.BOTTOM)
                    constraintSet.applyTo(binding.clViewOrders)
                }
                else {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.clViewOrders)
                    constraintSet.setVisibility(binding.tvCancelledOrders.id, View.VISIBLE)
                    constraintSet.setVisibility(binding.rvCancelledOrders.id, View.VISIBLE)
                    constraintSet.connect(binding.tvDeliveredOrders.id, ConstraintSet.TOP, binding.rvCancelledOrders.id, ConstraintSet.BOTTOM)
                    constraintSet.applyTo(binding.clViewOrders)
                }
                if (activeOrdersAdapter.orders.isEmpty() && cancelledOrdersAdapter.orders.isEmpty() && deliveredOrdersAdapter.orders.isEmpty()) {  //called here because at this point everything is initialized
                    binding.ivEmptyOrder.visibility = View.VISIBLE
                    binding.tvEmptyOrder.visibility = View.VISIBLE
                }
                else {
                    binding.ivEmptyOrder.visibility = View.INVISIBLE
                    binding.tvEmptyOrder.visibility = View.INVISIBLE
                }
                binding.rvCancelledOrders.adapter =  cancelledOrdersAdapter
        }
        cancelledOrdersAdapter.setOnClickListener(object: ActiveCancelledOrdersAdapter.OnClickListener {
            override fun onClick(position: Int, order: Order) {
                redirectToViewParticularOrderFragment(order.orderId)
            }
        })
    }

    private suspend fun setUpActiveOrdersAdapter() {
        lifecycleScope.launch {
            viewModel.getActiveOrders().observe(viewLifecycleOwner) {
                activeOrdersAdapter.orders = it
                activeOrdersAdapter.notifyDataSetChanged()
                binding.rvActiveOrders.adapter =  activeOrdersAdapter
                lifecycleScope.launch {
                    setUpDeliveredOrdersAdapter()
                }
                if (activeOrdersAdapter.orders.isEmpty()) {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.clViewOrders)
                    constraintSet.setVisibility(binding.tvActiveOrders.id, View.GONE)
                    constraintSet.setVisibility(binding.rvActiveOrders.id, View.GONE)
                    constraintSet.connect(binding.tvCancelledOrders.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    constraintSet.applyTo(binding.clViewOrders)
                }
                else {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.clViewOrders)
                    constraintSet.setVisibility(binding.tvActiveOrders.id, View.VISIBLE)
                    constraintSet.setVisibility(binding.rvActiveOrders.id, View.VISIBLE)
                    constraintSet.connect(binding.tvCancelledOrders.id, ConstraintSet.TOP, binding.rvActiveOrders.id, ConstraintSet.BOTTOM)
                    constraintSet.applyTo(binding.clViewOrders)
                }
                if (activeOrdersAdapter.orders.isEmpty() && cancelledOrdersAdapter.orders.isEmpty() && deliveredOrdersAdapter.orders.isEmpty()) {  //called here because at this point everything is initialized
                    binding.ivEmptyOrder.visibility = View.VISIBLE
                    binding.tvEmptyOrder.visibility = View.VISIBLE
                }
                else {
                    binding.ivEmptyOrder.visibility = View.INVISIBLE
                    binding.tvEmptyOrder.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setUpActiveOrdersRecyclerView() {
        binding.rvActiveOrders.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launch {
                setUpActiveOrdersAdapter()
        }
        activeOrdersAdapter.setOnClickListener(object: ActiveCancelledOrdersAdapter.OnClickListener {
            override fun onClick(position: Int, order: Order) {
                redirectToViewOrderDispatchedFragment(order.orderId)
            }
        })
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
        activityViewModel.selectedMenu = Screens.ORDER_SCREEN
        setUpActiveOrdersRecyclerView()
        setUpCancelledOrdersRecyclerView()
        setUpDeliveredOrdersRecyclerView()
    }
}
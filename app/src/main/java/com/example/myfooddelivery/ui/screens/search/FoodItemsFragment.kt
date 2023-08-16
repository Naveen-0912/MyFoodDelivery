package com.example.myfooddelivery.ui.screens.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfooddelivery.R
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FragmentSearchBinding
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.neworder.NewOrderFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FoodItemsFragment: Fragment() {

    private val binding: FragmentSearchBinding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val foodItemsAdapter: FoodItemsAdapter by lazy { FoodItemsAdapter(mutableListOf(), requireContext()) }
    private val viewModel: SearchViewModel by viewModels(
        {requireParentFragment()}
    ) { SearchViewModelFactory(DatabaseProvider(activity?.baseContext!!)) }

    private fun redirectToOrderFragment(hotelId: Int) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
        requireActivity().supportFragmentManager.commit {
            val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
            viewModel.hotelId = hotelId
            setCustomAnimations(R.anim.pop_fade_in, 0, 0, R.anim.pop_fade_out)
            requireActivity().supportFragmentManager.setFragmentResult(
                FragmentResultConstants.FRAGMENT_RESULT_KEY.name,
                bundleOf(FragmentResultConstants.HOTEL_ID_KEY.name to hotelId)
            )
            add(activityBaseBinding.flBaseFragment.id, NewOrderFragment())
            addToBackStack(null)
        }

    }

    private fun displayEmptyResults() {
        if (foodItemsAdapter.items.isEmpty()) {
            binding.tvEmptyItems.visibility = View.VISIBLE
            binding.ivEmptyItems.visibility = View.VISIBLE
        }
        else {
            binding.tvEmptyItems.visibility = View.INVISIBLE
            binding.ivEmptyItems.visibility = View.INVISIBLE
        }
    }

    private fun setUpRecyclerView() {
        foodItemsAdapter.setOnClickListener(object: FoodItemsAdapter.OnClickListener {
            override fun onClick(position: Int, item: Item) {
                redirectToOrderFragment(item.hotelId)
            }
        })
        binding.rvFoodAndHotel.adapter =  foodItemsAdapter
        binding.rvFoodAndHotel.layoutManager = LinearLayoutManager(activity?.baseContext)
        lifecycleScope.launch(Dispatchers.IO) {
            foodItemsAdapter.items = viewModel.getFilteredItems()
            withContext(Dispatchers.Main) {
                foodItemsAdapter.notifyDataSetChanged()
                displayEmptyResults()
            }
        }
    }

    private fun applyFilters() {
        viewModel.searchText.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                foodItemsAdapter.items = viewModel.getFilteredItems()
                withContext(Dispatchers.Main) {
                    foodItemsAdapter.notifyDataSetChanged()
                    displayEmptyResults()
                }
            }
        }
        viewModel.isVegFilterApplied.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                foodItemsAdapter.items = viewModel.getFilteredItems()
                withContext(Dispatchers.Main) {
                    foodItemsAdapter.notifyDataSetChanged()
                    displayEmptyResults()
                }
            }
        }
        viewModel.sortTypeFood.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                foodItemsAdapter.items = viewModel.getFilteredItems()
                withContext(Dispatchers.Main) {
                    foodItemsAdapter.notifyDataSetChanged()
                    displayEmptyResults()
                }
            }
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

        setUpRecyclerView()
        applyFilters()
    }
}
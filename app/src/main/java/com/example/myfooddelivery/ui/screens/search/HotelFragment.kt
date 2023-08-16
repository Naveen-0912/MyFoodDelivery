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
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.databinding.ActivityBaseBinding
import com.example.myfooddelivery.databinding.FragmentSearchBinding
import com.example.myfooddelivery.constants.FragmentResultConstants
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.screens.neworder.NewOrderFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HotelFragment: Fragment() {

    private val binding: FragmentSearchBinding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val hotelAdapter: HotelAdapter by lazy { HotelAdapter(mutableListOf()) }

    private val viewModel: SearchViewModel by viewModels(
        {requireParentFragment()}
    ) { SearchViewModelFactory(DatabaseProvider(requireActivity()))}

    private fun redirectToOrderFragment(hotelId: Int) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
        requireActivity().supportFragmentManager.commit {
            val activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
            requireActivity().supportFragmentManager.setFragmentResult(
                FragmentResultConstants.FRAGMENT_RESULT_KEY.name,
                bundleOf(FragmentResultConstants.HOTEL_ID_KEY.name to hotelId)
            )
            add(activityBaseBinding.flBaseFragment.id, NewOrderFragment())
            addToBackStack(null)
        }
    }

    private fun displayEmptyResults() {
        if (hotelAdapter.hotels.isEmpty()) {
            binding.tvEmptyItems.visibility = View.VISIBLE
            binding.ivEmptyItems.visibility = View.VISIBLE
        }
        else {
            binding.tvEmptyItems.visibility = View.INVISIBLE
            binding.ivEmptyItems.visibility = View.INVISIBLE
        }
    }

    private fun setUpRecyclerView() {
        binding.rvFoodAndHotel.adapter =  hotelAdapter
        binding.rvFoodAndHotel.layoutManager = LinearLayoutManager(activity?.baseContext)
        lifecycleScope.launch (Dispatchers.IO){
            hotelAdapter.hotels = viewModel.getFilteredHotels()
            withContext(Dispatchers.Main) {
                hotelAdapter.notifyDataSetChanged()
                displayEmptyResults()
            }
        }

        hotelAdapter.setOnClickListener(object: HotelAdapter.OnClickListener {
            override fun onClick(position: Int, hotelAccount: HotelAccount) {
                redirectToOrderFragment(hotelAccount.hotelId)
            }
        })
    }

    private fun applyFilters() {
        viewModel.searchText.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                hotelAdapter.hotels = viewModel.getFilteredHotels()
                withContext(Dispatchers.Main) {
                    hotelAdapter.notifyDataSetChanged()
                    displayEmptyResults()
                }
            }
        }
        viewModel.isVegFilterApplied.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                hotelAdapter.hotels = viewModel.getFilteredHotels()
                withContext(Dispatchers.Main) {
                    hotelAdapter.notifyDataSetChanged()
                    displayEmptyResults()
                }
            }
        }
        viewModel.sortTypeHotel.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                hotelAdapter.hotels = viewModel.getFilteredHotels()
                withContext(Dispatchers.Main) {
                    hotelAdapter.notifyDataSetChanged()
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
        //changed
    }
}
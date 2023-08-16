package com.example.myfooddelivery.ui.screens.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myfooddelivery.data.db.entities.HotelAccount
import com.example.myfooddelivery.data.db.entities.Item
import com.example.myfooddelivery.constants.SortType
import com.example.myfooddelivery.constants.ViewPagerMenus
import com.example.myfooddelivery.ui.helper.DatabaseProvider

class SearchViewModel(private val databaseProvider: DatabaseProvider): ViewModel() {

    var searchText = MutableLiveData("")
    var isVegFilterApplied = MutableLiveData(false)
    var sortTypeFood = MutableLiveData(SortType.NONE)
    var sortTypeHotel = MutableLiveData(SortType.NONE)
    var selectedOption = ViewPagerMenus.HOTEL_TAB
    var hotelId = -1
    var selectedCategory: String = ""

    private fun getHotelsSortedByRatings(): List<HotelAccount> {
        return if (isVegFilterApplied.value!!)
            databaseProvider.hotelAccountRepository.getVegHotelsFromSubStringSortedByRatings(
                searchText.value!!
            )
        else
            databaseProvider.hotelAccountRepository.getAllHotelsFromSubStringSortedByRatings(
                searchText.value!!
            )
    }

    private fun getHotelsWithNoSorting(): List<HotelAccount> {
        return if (isVegFilterApplied.value!!)
            databaseProvider.hotelAccountRepository.getVegHotelsFromSubString(searchText.value!!)
        else
            databaseProvider.hotelAccountRepository.getHotelAccountsFromSubString(searchText.value!!)
    }

    fun getFilteredHotels(): List<HotelAccount> {
        return when(sortTypeHotel.value!!) {
            SortType.RATINGS -> getHotelsSortedByRatings()
            else -> getHotelsWithNoSorting()
        }
    }

    private fun getItemsSortedByRatings(): List<Item> {
        return if (isVegFilterApplied.value!!)
            databaseProvider.itemRepository.getVegItemsFromSubStringSortedByRatings(
                searchText.value!!
            )
        else
            databaseProvider.itemRepository.getAllItemsFromSubStringSortedByRatings(
                searchText.value!!
            )
    }

    private fun getItemsSortedByPriceHighToLow(): List<Item> {
        return if (isVegFilterApplied.value!!)
            databaseProvider.itemRepository.getVegItemsFromSubStringSortedByPriceHighToLow(searchText.value!!)
        else
            databaseProvider.itemRepository.getAllItemsFromSubStringSortedByPriceHighToLow(searchText.value!!)
    }

    private fun getItemsSortedByPriceLowToHigh(): List<Item> {
        return if (isVegFilterApplied.value!!)
            databaseProvider.itemRepository.getVegItemsFromSubStringSortedByPriceLowToHigh(searchText.value!!)
        else
            databaseProvider.itemRepository.getAllItemsFromSubStringSortedByPriceLowToHigh(searchText.value!!)
    }

    private fun getItemsWithNoSorting(): List<Item> {
        return if (isVegFilterApplied.value!!)
            databaseProvider.itemRepository.getVegItemsFromSubString(searchText.value!!)
        else
            databaseProvider.itemRepository.getAllItemsFromSubString(searchText.value!!)
    }

    fun getFilteredItems(): List<Item> {
        return when(sortTypeFood.value!!) {
            SortType.RATINGS -> getItemsSortedByRatings()
            SortType.PRICE_HIGH_TO_LOW -> getItemsSortedByPriceHighToLow()
            SortType.PRICE_LOW_TO_HIGH -> getItemsSortedByPriceLowToHigh()
            else -> getItemsWithNoSorting()
        }
    }
}
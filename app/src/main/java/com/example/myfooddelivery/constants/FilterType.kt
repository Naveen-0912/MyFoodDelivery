package com.example.myfooddelivery.constants

import com.example.myfooddelivery.R
import com.example.myfooddelivery.ui.helper.SortFilterResourceWrapper

enum class FilterType(val sortFilterResourceWrapper: SortFilterResourceWrapper) {
    VEG(SortFilterResourceWrapper("Veg", "Veg", R.drawable.veg_indicator)),
    NONE(SortFilterResourceWrapper("None", "None", R.drawable.baseline_none_24)),
    NONE_DARK_MODE(SortFilterResourceWrapper("None", "None", R.drawable.baseline_none_24_white))
}
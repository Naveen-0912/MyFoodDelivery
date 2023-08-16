package com.example.myfooddelivery.constants

import com.example.myfooddelivery.R
import com.example.myfooddelivery.ui.helper.SortFilterResourceWrapper

enum class SortType(val sortFilterResourceWrapper: SortFilterResourceWrapper) {
    RATINGS(SortFilterResourceWrapper("Ratings", "Ratings", R.drawable.baseline_star_rate_24)),
    PRICE_LOW_TO_HIGH(SortFilterResourceWrapper("Cost Increasing", "Cost", R.drawable.baseline_arrow_upward_24)),
    PRICE_HIGH_TO_LOW(SortFilterResourceWrapper("Cost Decreasing", "Cost", R.drawable.baseline_arrow_downward_24)),
    NONE(SortFilterResourceWrapper("None", "None", R.drawable.baseline_none_24)),
    NONE_DARK_MODE(SortFilterResourceWrapper("None", "None", R.drawable.baseline_none_24_white))
}
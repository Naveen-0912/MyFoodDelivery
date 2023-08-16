package com.example.myfooddelivery.constants

import com.example.myfooddelivery.R
import com.example.myfooddelivery.ui.helper.FoodCategoryResourceWrapper

enum class FoodCategories(val categoryWrapper: FoodCategoryResourceWrapper) {
    BIRYANI(FoodCategoryResourceWrapper("Biryani", R.drawable.cate_biryani)),
    RICE(FoodCategoryResourceWrapper("Rice", R.drawable.cate_rice)),
    PIZZA(FoodCategoryResourceWrapper("Pizza", R.drawable.cate_pizza)),
    BURGER(FoodCategoryResourceWrapper("Burger", R.drawable.cate_burger)),
    CHICKEN(FoodCategoryResourceWrapper("Chicken", R.drawable.cate_chicken)),
    FRIES(FoodCategoryResourceWrapper("Fries", R.drawable.cate_fries)),
    PANEER(FoodCategoryResourceWrapper("Paneer", R.drawable.cate_paneer)),
    NAAN(FoodCategoryResourceWrapper("Naan", R.drawable.cate_naan)),
    PARATHA(FoodCategoryResourceWrapper("Paratha", R.drawable.cate_paratha))
}
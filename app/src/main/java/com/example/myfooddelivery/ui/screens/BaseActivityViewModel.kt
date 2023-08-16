package com.example.myfooddelivery.ui.screens

import androidx.lifecycle.ViewModel
import com.example.myfooddelivery.constants.Screens

class BaseActivityViewModel: ViewModel() {
    var selectedMenu: Screens = Screens.HOME_SCREEN
}
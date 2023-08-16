package com.example.myfooddelivery.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.constants.ValidationSuccessConstant
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.validations.UserInputValidations
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val databaseProvider: DatabaseProvider,
    private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper) : ViewModel() {

    var isConfigChanged = false
    var mobileNo = ""
    var password = ""

    fun validateMobileNo(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateMobileNo(mobileNo)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name)
            textInputLayout.error = result
        else
            textInputLayout.error = null
    }

    suspend fun isUserLoggedIn() = loggedInAccountDataStoreHelper.isUserLoggedIn()

    private suspend fun checkCustomerAccounts() = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            databaseProvider.customerAccountRepository.getAllAccounts()
                .find { it.mobileNo == mobileNo && it.authenticate(password) }
        }

    suspend fun authenticateUser(context: Context): Boolean {
        val account = checkCustomerAccounts()
        if(account != null){
            LoggedInAccountDataStoreHelper(context).storeLoggedInAccount(account)
            return true
        }
        return false
    }

}
package com.example.myfooddelivery.ui.screens.updateprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.datastore.LoggedInAccountDataStoreHelper
import com.example.myfooddelivery.data.db.entities.Address
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.constants.CustomerAccountFields
import com.example.myfooddelivery.constants.ValidationSuccessConstant
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.validations.UserInputValidations
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateProfileViewModel(private val loggedInAccountDataStoreHelper: LoggedInAccountDataStoreHelper,
                             private val databaseProvider: DatabaseProvider
) : ViewModel() {
    var name = ""; var street = ""; var area = ""
    var state = "";  var isVeg = true
    var no = ""; var pinCode = ""
    val errorFields = mutableListOf<CustomerAccountFields>()

    fun validateName(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateName(name)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.NAME)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.NAME)
        }
    }
    fun validateNo(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateNo(no)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.NO)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.NO)
        }
    }
    fun validateStreet(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateStreet(street)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.STREET)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.STREET)
        }
    }
    fun validateArea(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateArea(area)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.AREA)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.AREA)
        }
    }
    fun validateState(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateState(state)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.STATE)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.STATE)
        }
    }
    fun validatePinCode(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validatePinCode(pinCode)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.PIN_CODE)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.PIN_CODE)
        }
    }

    fun loadAccountDetails() {
        viewModelScope.launch {
            val loggedInAccount = loggedInAccountDataStoreHelper.getLoggedInAccount()!!
            name = loggedInAccount.name
            no = loggedInAccount.address.no.toString()
            street = loggedInAccount.address.street
            area = loggedInAccount.address.area
            state = loggedInAccount.address.state
            pinCode = loggedInAccount.address.pinCode.toString()
        }
    }

    suspend fun updateUser(): Boolean {
        return withContext(Dispatchers.IO) {
            val address = Address(no.toInt(), street, area, state, pinCode.toInt())
            val loggedInAccount = loggedInAccountDataStoreHelper.getLoggedInAccount()!!
            val account = CustomerAccount(
                name,
                address,
                loggedInAccount.mobileNo,
                loggedInAccount.passwordWrapper,
                loggedInAccount.favoriteHotels,
                loggedInAccount.customerId
            )
            loggedInAccountDataStoreHelper.storeLoggedInAccount(account)
            databaseProvider.customerAccountRepository.updateAccount(account)
        }
    }
}
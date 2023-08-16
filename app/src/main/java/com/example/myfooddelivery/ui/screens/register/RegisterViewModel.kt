package com.example.myfooddelivery.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfooddelivery.data.db.entities.Account
import com.example.myfooddelivery.data.db.entities.Address
import com.example.myfooddelivery.data.db.entities.CustomerAccount
import com.example.myfooddelivery.constants.CustomerAccountFields
import com.example.myfooddelivery.constants.ValidationSuccessConstant
import com.example.myfooddelivery.data.repository.CustomerAccountRepository
import com.example.myfooddelivery.ui.helper.DatabaseProvider
import com.example.myfooddelivery.ui.validations.UserInputValidations
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterViewModel(private val databaseProvider: DatabaseProvider) : ViewModel() {
    var isConfigChanged = false
    var name = ""; var street = ""; var area = ""
    var state = ""; var mobileNo = ""; var password = ""; var confirmPassword = ""
    var no = ""; var pinCode = ""
    val errorFields = CustomerAccountFields.values().toMutableSet()

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
    fun validateMobileNo(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateMobileNo(mobileNo)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.MOBILE_NO)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.MOBILE_NO)
        }
    }
    fun validatePassword(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validatePassword(password)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.PASSWORD)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.PASSWORD)
        }
    }
    fun validateConfirmPassword(textInputLayout: TextInputLayout) {
        val result = UserInputValidations.validateConfirmPassword(password, confirmPassword)
        if (result != ValidationSuccessConstant.SUCCESS_STR.name) {
            textInputLayout.error = result
            errorFields.add(CustomerAccountFields.CONFIRM_PASSWORD)
        }
        else {
            textInputLayout.error = null
            errorFields.remove(CustomerAccountFields.CONFIRM_PASSWORD)
        }
    }

    private suspend fun insertAccountIntoDatabase(accountRepository: CustomerAccountRepository, account: CustomerAccount) =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            return@withContext accountRepository.insertAccount(account)
        }

    private suspend fun isMobileNoExisting() =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            if (databaseProvider.customerAccountRepository.getCustomerByMobileNo(mobileNo) != null)
                return@withContext true
            if (databaseProvider.hotelAccountRepository.getHotelByMobileNo(mobileNo) != null)
                return@withContext true
            return@withContext false
        }

    suspend fun registerUser(): Boolean {
        if(isMobileNoExisting())
            return false
        val address = Address(no.toInt(), street, area, state, pinCode.toInt())
        val passwordWrapper = Account.PasswordWrapper(password)

        val account = CustomerAccount(name, address, mobileNo, passwordWrapper, listOf())
        return insertAccountIntoDatabase(databaseProvider.customerAccountRepository, account)
    }
}
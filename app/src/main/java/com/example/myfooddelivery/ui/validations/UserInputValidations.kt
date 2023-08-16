package com.example.myfooddelivery.ui.validations

import com.example.myfooddelivery.constants.InputValidationConstants
import com.example.myfooddelivery.constants.ValidationSuccessConstant

object UserInputValidations {

    fun validateName(name: String): String {
        if (name.isBlank() || StringValidator.containsSpecialCharacter(name) || StringValidator.containsDigits(name))
            return "Invalid Name"
        if (name.length > InputValidationConstants.NAME.value)
            return "Name must not exceed ${InputValidationConstants.NAME.value} characters"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validateNo(no: String): String {
        if(no.isBlank())
            return "Invalid No"
        val convertedNo = no.toInt()
        if (convertedNo > InputValidationConstants.NO_MAX.value || convertedNo < 1)
            return "Invalid No"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validateStreet(street: String): String {
        if (street.isBlank())
            return "Invalid Street"
        if (street.length > InputValidationConstants.STREET.value)
            return "Street must not exceed ${InputValidationConstants.STREET.value} characters"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validateArea(area: String): String {
        if (area.isBlank())
            return "Invalid Area"
        if(area.length > InputValidationConstants.AREA.value)
            return "Area must not exceed ${InputValidationConstants.AREA.value} characters"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validateState(state: String): String {
        if (state.isBlank() || StringValidator.containsSpecialCharacter(state) || StringValidator.containsDigits(state))
            return "Invalid State"
        if (state.length > InputValidationConstants.STATE.value)
            return "State must not exceed ${InputValidationConstants.STATE.value} characters"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validatePinCode(pinCode: String): String {
        if (pinCode.length != 6)
            return "Invalid No"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validateMobileNo(mobileNo: String): String {
        if (mobileNo.isBlank())
            return "Invalid Mobile No"
        if (mobileNo.length != InputValidationConstants.MOBILE_NO.value)
            return "Mobile Number must be ${InputValidationConstants.MOBILE_NO.value} digits"
        if(mobileNo.first().digitToInt() in 0..5)
            return "Invalid Mobile No"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validatePassword(password: String): String {
        if(password.length < InputValidationConstants.PASSWORD_MIN.value)
            return "Password must be atleast ${InputValidationConstants.PASSWORD_MIN.value} characters"
        if(password.length > InputValidationConstants.PASSWORD_MAX.value)
            return "Password must not exceed ${InputValidationConstants.PASSWORD_MAX.value} characters"
        if(!StringValidator.containsSpecialCharacter(password) ||
            !StringValidator.containsLowerCaseCharacter(password) ||
            !StringValidator.containsUpperCaseCharacter(password) ||
            !StringValidator.containsDigits(password))
            return "Password must contain atleast 1 uppercase, 1 lowercase, 1 digit and 1 special character"
        return ValidationSuccessConstant.SUCCESS_STR.name
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String {
        return if (password == confirmPassword) ValidationSuccessConstant.SUCCESS_STR.name else "Passwords Don't match"
    }
}
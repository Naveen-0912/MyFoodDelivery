package com.example.myfooddelivery.ui.validations

import com.example.myfooddelivery.constants.DigitsConstant
import com.example.myfooddelivery.constants.SpecialCharactersConstant

object StringValidator {
    fun containsSpecialCharacter(input: String) : Boolean {
        input.find { SpecialCharactersConstant.SPECIAL_CHARACTERS.contains(it.toString()) }?.let { return true }
        return false
    }

    fun containsDigits(input: String): Boolean {
        input.find { DigitsConstant.DIGITS.contains(it.toString()) }?.let { return true }
        return false
    }

    fun containsLowerCaseCharacter(input: String) : Boolean {
        for (char in input)
            if (char.isLowerCase())
                return true
        return false
//        input.find { it in 'a'..'z' }?.let { return true }
//        return false
    }

    fun containsUpperCaseCharacter(input: String) : Boolean {
        for (char in input)
            if (char.isUpperCase())
                return true
        return false
//        input.find { it in 'A'..'Z' }?.let { return true }
//        return false
    }
}
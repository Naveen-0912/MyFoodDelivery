package com.example.myfooddelivery.constants

enum class InputValidationConstants(val value: Int) {
    NAME(20), STREET(20), AREA(20), STATE(20), NO_MAX(1000),
    MOBILE_NO(10), PASSWORD_MIN(8), PASSWORD_MAX(20)
}
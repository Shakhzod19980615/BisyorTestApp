package com.example.testapp.domain.use_case.authoration

class PhoneNumberValidationUseCase {

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        // Remove any spaces or special characters from the phone number
        val cleanPhoneNumber = phoneNumber.replace("\\s+".toRegex(), "")
        // Check if the phone number is empty or null
        if (cleanPhoneNumber.isEmpty()) {
            return false
        }

        if (phoneNumber.length != 9) {
            return false
        }
         return true
    }
}
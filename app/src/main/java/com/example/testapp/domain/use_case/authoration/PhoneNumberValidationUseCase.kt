package com.example.testapp.domain.use_case.authoration

class PhoneNumberValidationUseCase {

    fun isPhoneNumberValid(phoneNumber: String): Pair<Boolean, String?> {
        // Remove any spaces or special characters from the phone number
        val cleanPhoneNumber = phoneNumber.replace("\\s+".toRegex(), "")
        // Check if the phone number is empty or null
        if (cleanPhoneNumber.isEmpty()) {
            return Pair(false, "Phone number cannot be empty.")
        }

        if (phoneNumber.length != 9) {
            return Pair(false, "Phone number must be 9 digits long.")
        }
         return Pair(true, null)
    }
}
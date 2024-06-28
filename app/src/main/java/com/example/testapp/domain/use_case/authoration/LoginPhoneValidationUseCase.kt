package com.example.testapp.domain.use_case.authoration

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.example.testapp.R
import javax.inject.Inject

class LoginPhoneValidationUseCase @Inject constructor() {
    fun isPhoneNumberValid(phoneNumber: String, context:Context): Pair<Boolean, String?> {
        // Remove any spaces or special characters from the phone number
        val cleanPhoneNumber = phoneNumber.replace("\\s+".toRegex(), "")
        // Check if the phone number is empty or null
        if (cleanPhoneNumber.isEmpty()) {
            return Pair(false, getString(context, R.string.warning_enter_phone_number))
        }

        if (phoneNumber.length < 12) {
            return Pair(false, getString(context, R.string.warning_enter_phone_number))
        }
        return Pair(true, null)
    }
}
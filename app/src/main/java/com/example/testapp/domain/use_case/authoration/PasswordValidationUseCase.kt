package com.example.testapp.domain.use_case.authoration

import javax.inject.Inject

class PasswordValidationUseCase @Inject constructor() {

    fun isPasswordValid(password: String): Pair<Boolean, String?> {
        if (password.length < 6) {
            return Pair(false, "Password must be at least 6 characters long.")
        }
        if (password.isEmpty()) {
            return Pair(false, "Password cannot be empty.")
        }
        return Pair(true, null) // Password is valid
    }
}
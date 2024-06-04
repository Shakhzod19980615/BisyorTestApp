package com.example.testapp.domain.use_case.authoration

import javax.inject.Inject

class ConfirmationCodeValidationUseCase @Inject constructor()  {

    fun isConfirmationCodeValid(confirmationCode: String): Pair<Boolean, String?> {
        if (confirmationCode.length < 5) {
            return Pair(false, "Confirmation code must be 6 digits long.")
        }
        if (confirmationCode.isEmpty()) {
            return Pair(false, "Confirmation code cannot be empty.")
        }
        return Pair(true, null) // Confirmation code is valid
    }
}
package com.example.testapp.presentation.authoration.verificationCode.viewModel

import androidx.lifecycle.ViewModel
import com.example.testapp.domain.use_case.authoration.ConfirmationCodeValidationUseCase
import com.example.testapp.domain.use_case.authoration.VerifyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val confirmationCodeValidationUseCase: ConfirmationCodeValidationUseCase
): ViewModel() {

}
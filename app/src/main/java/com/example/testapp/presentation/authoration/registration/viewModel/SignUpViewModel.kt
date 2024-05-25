package com.example.testapp.presentation.authoration.registration.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.Resource
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.use_case.authoration.PasswordValidationUseCase
import com.example.testapp.domain.use_case.authoration.PhoneNumberValidationUseCase
import com.example.testapp.domain.use_case.authoration.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
       private val signUpUseCase: SignUpUseCase,
        private val phoneNumberValidationUseCase: PhoneNumberValidationUseCase,
       private val passwordValidationUseCase: PasswordValidationUseCase
): ViewModel(){
    private val _signUp = MutableStateFlow<Resource<BasicResponseModel>>(Resource.Loading())
    private val _phoneNumberValidation = MutableSharedFlow<Pair<Boolean,String?>>(replay = 1)
    val phoneNumberValidation: SharedFlow<Pair<Boolean, String?>> get() = _phoneNumberValidation.asSharedFlow()
    private val _passwordValidation = MutableSharedFlow<Pair<Boolean, String?>>(replay = 1)
    val passwordValidation: SharedFlow<Pair<Boolean, String?>> get() = _passwordValidation.asSharedFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun signUp(login: String, password: String, phoneNumber: String) {
        val isPhoneNumberValid = phoneNumberValidationUseCase.isPhoneNumberValid(phoneNumber)
        // Communicate validation result to the view
                _phoneNumberValidation.tryEmit(isPhoneNumberValid)

        if (!isPhoneNumberValid.first) {
            // Communicate validation result to the view
            return
        }
        val passwordValidationResult = passwordValidationUseCase.isPasswordValid(password)
        _passwordValidation.tryEmit(passwordValidationResult)

        if (!passwordValidationResult.first) {
            // Stop the signUp process if password is invalid
            return
        }
        val request = RegistrationRequest(login, password)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val result = kotlin.runCatching {
                    signUpUseCase.invoke(registrationRequest = request)
                }
                result.onSuccess {
                    _signUp.value = Resource.Success(it)
                }.onFailure {
                    _signUp.value = Resource.Error(it.message ?: "An unexpected error occured", null)
                }

                /*try {
                    val response = signUpUseCase.invoke(registrationRequest = request)
                    _signUp.value = Resource.Success(response)
                } catch (e: HttpException) {
                    (Resource.Error( "An unexpected error occured", null))
            }catch (e: IOException) {
                (Resource.Error("Couldn't reach server. Check your internet connection.", null))
            }*/
        }
    }
}
}
package com.example.testapp.presentation.home.viewModel.authoration

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.Resource
import com.example.testapp.data.request.RegistrationRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.use_case.authoration.PhoneNumberValidationUseCase
import com.example.testapp.domain.use_case.authoration.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
       private val signUpUseCase: SignUpUseCase,
        private val phoneNumberValidationUseCase: PhoneNumberValidationUseCase
): ViewModel(){
    private val _signUp = MutableStateFlow<Resource<BasicResponseModel>>(Resource.Loading())
    private val _phoneNumberValidation = MutableStateFlow<Boolean?>(null)
    val phoneNumberValidation = _phoneNumberValidation.asStateFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun signUp(login: String, password: String) {
        val isPhoneNumberValid = phoneNumberValidationUseCase.isPhoneNumberValid(login)
        if (!isPhoneNumberValid) {
            // Communicate validation result to the view
            _phoneNumberValidation.value = false
            return
        }
        val request = RegistrationRequest(login, password)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val response = signUpUseCase.invoke(registrationRequest = request)
                    _signUp.value = Resource.Success(response)
                } catch (e: HttpException) {
                    (Resource.Error( "An unexpected error occured", null))
            }catch (e: IOException) {
                (Resource.Error("Couldn't reach server. Check your internet connection.", null))
            }
        }
    }
}
}
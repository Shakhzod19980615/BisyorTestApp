package com.example.testapp.presentation.authoration.registration.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject



@HiltViewModel
class SignUpViewModel @Inject constructor(
       private val signUpUseCase: SignUpUseCase,
        private val phoneNumberValidationUseCase: PhoneNumberValidationUseCase,
       private val passwordValidationUseCase: PasswordValidationUseCase,
       private val errorParser: ErrorParser
): ViewModel(){
    val signUp = MutableStateFlow<Resource<BasicResponseModel>>(Resource.Loading())
    private val _phoneNumberValidation = MutableSharedFlow<Pair<Boolean,String?>>(replay = 1)
    val phoneNumberValidation: SharedFlow<Pair<Boolean, String?>> get() = _phoneNumberValidation.asSharedFlow()
    private val _passwordValidation = MutableSharedFlow<Pair<Boolean, String?>>(replay = 1)
    val passwordValidation: SharedFlow<Pair<Boolean, String?>> get() = _passwordValidation.asSharedFlow()
    fun validatePhoneNumber(phoneNumber: String) {
        val result = phoneNumberValidationUseCase.isPhoneNumberValid(phoneNumber)
        _phoneNumberValidation.tryEmit(result)
    }
    fun validatePassword(password: String) {
        val result = passwordValidationUseCase.isPasswordValid(password)
        _passwordValidation.tryEmit(result)
    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun signUp(login: String, password: String, context: Context) {
        val request = RegistrationRequest(login, password)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    signUpUseCase.invoke(registrationRequest = request)
                }.onSuccess {
                    signUp.value = Resource.Success(it)
                   // _signUp.value = Resource.Success(it)
                }.onFailure { throwable->
                    when (throwable) {
                        is HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    signUp.value = Resource.Error(parsedError.message)
                                } else {
                                    signUp.value = Resource.Error("Unknown error")
                                }
                            } else {
                                signUp.value = Resource.Error("Unknown error")
                            }
                        }
                    }

                }

        }
    }
}
}
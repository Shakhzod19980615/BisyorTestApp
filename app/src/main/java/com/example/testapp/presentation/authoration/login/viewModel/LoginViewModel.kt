package com.example.testapp.presentation.authoration.login.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.use_case.authoration.LoginPhoneValidationUseCase
import com.example.testapp.domain.use_case.authoration.LoginRequestUseCase
import com.example.testapp.domain.use_case.authoration.PasswordValidationUseCase
import com.example.testapp.domain.use_case.authoration.PhoneNumberValidationUseCase
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
class LoginViewModel @Inject constructor(
    private val loginRequestUseCase: LoginRequestUseCase,
    private val phoneNumberValidationUseCase: LoginPhoneValidationUseCase,
    private val passwordValidationUseCase: PasswordValidationUseCase,
    private val errorParser: ErrorParser
):ViewModel() {
    val signIn = MutableStateFlow<Resource<UserDataModel>>(Resource.Loading())
    private val _phoneNumberValidation = MutableSharedFlow<Pair<Boolean,String?>>(replay = 1)
    val phoneNumberValidation: SharedFlow<Pair<Boolean, String?>> get() = _phoneNumberValidation.asSharedFlow()
    private val _passwordValidation = MutableSharedFlow<Pair<Boolean, String?>>(replay = 1)
    val passwordValidation: SharedFlow<Pair<Boolean, String?>> get() = _passwordValidation.asSharedFlow()

    fun validatePhoneNumber(phoneNumber: String, context: Context) {
        val result = phoneNumberValidationUseCase.isPhoneNumberValid(phoneNumber,context)
        _phoneNumberValidation.tryEmit(result)
    }
    fun validatePassword(password: String) {
        val result = passwordValidationUseCase.isPasswordValid(password)
        _passwordValidation.tryEmit(result)
    }
    fun signIn(login: String, password: String,lang:String) {
        val loginRequest = LoginRequest(lang,login, password)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    loginRequestUseCase.invoke(loginRequest = loginRequest)
                }.onSuccess {
                    signIn.value = Resource.Success(it)
                }.onFailure {throwable->
                    when (throwable) {
                        is HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    signIn.value = Resource.Error(parsedError.message)
                                } else {
                                    signIn.value = Resource.Error("Unknown error")
                                }
                            } else {
                                signIn.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }
}
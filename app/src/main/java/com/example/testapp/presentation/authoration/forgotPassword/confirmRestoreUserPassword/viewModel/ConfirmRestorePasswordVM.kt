package com.example.testapp.presentation.authoration.forgotPassword.confirmRestoreUserPassword.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.resetPassword.ResetUserUpdatePasswordRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.use_case.authoration.PasswordValidationUseCase
import com.example.testapp.domain.use_case.authoration.ResetUpdatePasswordUseCase
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
class ConfirmRestorePasswordVM @Inject constructor(
    private val errorParser: ErrorParser,
    private val resetUpdatePasswordUseCase: ResetUpdatePasswordUseCase,
    private val passwordValidationUseCase: PasswordValidationUseCase,
):ViewModel() {

    val resetPassword = MutableStateFlow<Resource<UserDataModel>>(Resource.Loading())
    private val _passwordValidation = MutableSharedFlow<Pair<Boolean, String?>>(replay = 1)
    val passwordValidation: SharedFlow<Pair<Boolean, String?>> get() = _passwordValidation.asSharedFlow()
    fun validatePassword(password: String) {
        val result = passwordValidationUseCase.isPasswordValid(password)
        _passwordValidation.tryEmit(result)
    }

    fun resetPassword(login:String,code:String,password: String) {
        val request = ResetUserUpdatePasswordRequest(login,code,password)
        viewModelScope.launch {
            resetPassword.value = Resource.Loading()
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    resetUpdatePasswordUseCase.invoke(request)
                }.onSuccess {
                    resetPassword.value = Resource.Success(it)
                }.onFailure {throwable->
                    when (throwable) {
                        is HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    resetPassword.value = Resource.Error(parsedError.message)
                                } else {
                                    resetPassword.value = Resource.Error("Unknown error")
                                }
                            } else {
                                resetPassword.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }
}
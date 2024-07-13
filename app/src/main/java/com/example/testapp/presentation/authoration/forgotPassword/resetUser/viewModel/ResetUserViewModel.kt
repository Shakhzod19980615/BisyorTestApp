package com.example.testapp.presentation.authoration.forgotPassword.resetUser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.resetUser.ResetUserRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.use_case.authoration.LoginPhoneValidationUseCase
import com.example.testapp.domain.use_case.authoration.ResetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ResetUserViewModel @Inject constructor(
    private val resetUserUseCase: ResetUserUseCase,
    private val errorParser: ErrorParser,
    private val phoneNumberValidationUseCase: LoginPhoneValidationUseCase
): ViewModel() {
    val resetUser = MutableStateFlow<Resource<BasicResponseModel>>(Resource.Loading())

    fun resetUser(login: String) {
        val resetUserRequest = ResetUserRequest(login)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    resetUserUseCase.invoke(resetUserRequest = resetUserRequest)
                }.onSuccess {
                    resetUser.value = Resource.Success(it)
                }.onFailure {throwable->
                    when (throwable) {
                        is HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    resetUser.value = Resource.Error(parsedError.message)}
                                else{
                                    resetUser.value = Resource.Error("Unknown error")
                                }
                            }else{
                                resetUser.value = Resource.Error("Unknown error")
                            }
                            }
                    }
                }
            }
        }
    }
}
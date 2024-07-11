package com.example.testapp.presentation.authoration.verificationCode.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.use_case.authoration.ConfirmationCodeValidationUseCase
import com.example.testapp.domain.use_case.authoration.VerifyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase,
    private val confirmationCodeValidationUseCase: ConfirmationCodeValidationUseCase,
    private val errorParser: ErrorParser
): ViewModel() {
    val verifyCode = MutableStateFlow<Resource<UserDataModel>>(Resource.Loading())
    private val _confirmationCodeValidation = MutableSharedFlow<Pair<Boolean, String?>>(replay = 1)
    fun verifyCode(verificationCodeRequest: VerificationCodeRequest) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    verifyCodeUseCase.invoke(verificationCodeRequest = verificationCodeRequest)
                }.onSuccess {
                    verifyCode.value = Resource.Success(it)
                }.onFailure {throwable->
                    when (throwable) {
                        is HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    verifyCode.value = Resource.Error(parsedError.message)
                                } else {
                                    verifyCode.value = Resource.Error("Unknown error")
                                }
                            } else {
                                verifyCode.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }
}
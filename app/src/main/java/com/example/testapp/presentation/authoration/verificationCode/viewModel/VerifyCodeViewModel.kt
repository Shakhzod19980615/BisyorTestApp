package com.example.testapp.presentation.authoration.verificationCode.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ApiResponse
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.verificationCode.VerificationCodeRequest
import com.example.testapp.domain.model.basicResponseModel.BasicResponseModel
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.use_case.authoration.ConfirmationCodeValidationUseCase
import com.example.testapp.domain.use_case.authoration.VerifyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    val confirmationCodeValidation: SharedFlow<Pair<Boolean, String?>> get() = _confirmationCodeValidation.asSharedFlow()
    fun validateConfirmationCode(confirmationCode: String) {
        val result = confirmationCodeValidationUseCase.isConfirmationCodeValid(confirmationCode)
        _confirmationCodeValidation.tryEmit(result)
    }
    fun verifyCode(verificationCodeRequest: VerificationCodeRequest, context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    verifyCodeUseCase.invoke(verificationCodeRequest = verificationCodeRequest)
                }.onSuccess {
                    verifyCode.value = Resource.Success(it)
                }.onFailure {
                    val errorResponse = it as? Response<*>
                    errorResponse?.let { response ->
                        val parsedError = errorParser.parseError(response)
                        parsedError?.let { error ->
                            verifyCode.value = Resource.Error(error.message)
                        } ?: run {
                            verifyCode.value = Resource.Error("Unknown error")
                        }
                    } ?: run { verifyCode.value = Resource.Error(it.message.toString()) }
                }
            }
        }
    }
}
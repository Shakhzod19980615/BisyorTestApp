package com.example.testapp.presentation.authoration.forgotPassword.confirmUserRestore.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.resetUserConfirmRequest.ResetUserConfirmRequest
import com.example.testapp.domain.use_case.authoration.ResetUserConfirmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ConfirmUserRestoreVM @Inject constructor(
    private val resetUserConfirmUseCase: ResetUserConfirmUseCase,
    private val errorParser: ErrorParser
): ViewModel() {
    val confirmRestoreUser = MutableStateFlow<Resource<Boolean>>(Resource.Loading())

    fun restoreUserConfirm(restoreConfirmRequest: ResetUserConfirmRequest){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    resetUserConfirmUseCase.invoke(restoreConfirmRequest)
                }.onSuccess {
                    confirmRestoreUser.value = Resource.Success(it)
                }.onFailure {throwable->
                    when (throwable) {
                        is HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    confirmRestoreUser.value = Resource.Error(parsedError.message)
                                } else {
                                    confirmRestoreUser.value = Resource.Error("Unknown error")
                                }
                            } else {
                                confirmRestoreUser.value = Resource.Error("Unknown error")
                            }
    }
}
}
            }
        }
    }
}
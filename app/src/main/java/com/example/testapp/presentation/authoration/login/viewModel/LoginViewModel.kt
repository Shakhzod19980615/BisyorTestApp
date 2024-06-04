package com.example.testapp.presentation.authoration.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.Resource
import com.example.testapp.data.request.login.LoginRequest
import com.example.testapp.domain.model.userDataModel.UserDataModel
import com.example.testapp.domain.use_case.authoration.LoginRequestUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginRequestUseCase: LoginRequestUseCase
):ViewModel() {
    val login = MutableStateFlow<Resource<UserDataModel>>(Resource.Loading())

    fun validateLogin(login: String):Boolean {
        return login.isNotEmpty()
    }
    fun validatePassword(password: String):Boolean {
        return password.isNotEmpty()
    }
    fun signIn(loginRequest: LoginRequest) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    loginRequestUseCase.invoke(loginRequest = loginRequest)
                }.onSuccess {
                    login.value = Resource.Success(it)
                }.onFailure {
                    login.value = Resource.Error(it.message.toString())
                }
            }
        }
    }
}
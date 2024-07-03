package com.aloysius.dicoding.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aloysius.dicoding.data.ResultState
import com.aloysius.dicoding.data.remote.repository.UserRepository
import com.aloysius.dicoding.data.pref.UserModel
import com.aloysius.dicoding.data.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, password: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val response = repository.getLoginRepository().userLogin(email, password)) {
                    is ResultState.Success -> {
                        _loginResponse.postValue(response.data)
                    }
                    is ResultState.Error -> {
                        _errorMessage.postValue(response.error)
                    }
                    is ResultState.Loading -> {
                    }
                }
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveSession(user)
            }
        }
    }
}

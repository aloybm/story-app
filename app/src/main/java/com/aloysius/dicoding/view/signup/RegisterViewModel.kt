package com.aloysius.dicoding.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aloysius.dicoding.data.ResultState
import com.aloysius.dicoding.data.remote.repository.UserRepository
import com.aloysius.dicoding.data.remote.response.RegisterResponse

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>?> {
        return repository.getRegisterRepository().userRegister(name, email, password)
    }
}
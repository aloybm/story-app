package com.aloysius.dicoding.data.remote.repository

import com.aloysius.dicoding.data.pref.UserModel
import com.aloysius.dicoding.data.pref.UserPreference
import com.aloysius.dicoding.data.remote.repository.user.LoginRepository
import com.aloysius.dicoding.data.remote.repository.user.RegisterRepository
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val registerRepository: RegisterRepository,
    private val loginRepository: LoginRepository
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getRegisterRepository(): RegisterRepository {
        return registerRepository
    }

    fun getLoginRepository(): LoginRepository {
        return loginRepository
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            registerRepository: RegisterRepository,
            loginRepository: LoginRepository
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, registerRepository, loginRepository).also { instance = it }
            }
    }
}

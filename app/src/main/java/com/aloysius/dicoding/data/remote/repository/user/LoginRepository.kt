package com.aloysius.dicoding.data.remote.repository.user

import com.aloysius.dicoding.data.ResultState
import com.aloysius.dicoding.data.remote.response.ErrorResponse
import com.aloysius.dicoding.data.remote.response.LoginResponse
import com.aloysius.dicoding.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class LoginRepository private constructor(
    private val apiService: ApiService,
) {
    suspend fun userLogin(email: String, password: String): ResultState<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            ResultState.Success(response)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            ResultState.Error(errorResponse.message ?: "An error occurred")
        } catch (e: Exception) {
            ResultState.Error("An error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null

        fun getInstance(apiService: ApiService): LoginRepository =
            instance ?: synchronized(this) {
                instance ?: LoginRepository(apiService).also { instance = it }
            }
    }
}

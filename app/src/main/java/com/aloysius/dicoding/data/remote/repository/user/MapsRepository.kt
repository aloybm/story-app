package com.aloysius.dicoding.data.remote.repository.user

import android.util.Log
import com.aloysius.dicoding.data.ResultState
import com.aloysius.dicoding.data.remote.response.ErrorResponse
import com.aloysius.dicoding.data.remote.response.ListStoryItem
import com.aloysius.dicoding.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException

class MapsRepository private constructor(
    private val apiService: ApiService,
) {
    suspend fun getMaps(): ResultState<List<ListStoryItem>> {
        return try {
            val response = apiService.getStoriesWithLocation()
            val stories = response.body()?.listStory ?: emptyList()

            Log.d("MapsRepository", "Fetching stories with token: $stories")
            ResultState.Success(stories.filterNotNull())
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            ResultState.Error(errorResponse.message ?: "An error occurred")
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "An error occurred")
        }
    }


    companion object {
        @Volatile
        private var instance: MapsRepository? = null

        fun getInstance(apiService: ApiService): MapsRepository =

            instance ?: synchronized(this) {
                instance ?: MapsRepository(apiService).also { instance = it }
            }
    }
}
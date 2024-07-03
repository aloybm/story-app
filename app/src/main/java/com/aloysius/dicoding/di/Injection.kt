package com.aloysius.dicoding.di

import android.content.Context
import com.aloysius.dicoding.data.database.StoryDatabase
import com.aloysius.dicoding.data.pref.UserPreference
import com.aloysius.dicoding.data.pref.dataStore
import com.aloysius.dicoding.data.remote.repository.StoryPagingRepository
import com.aloysius.dicoding.data.remote.repository.UserRepository
import com.aloysius.dicoding.data.remote.repository.user.LoginRepository
import com.aloysius.dicoding.data.remote.repository.user.MapsRepository
import com.aloysius.dicoding.data.remote.repository.user.RegisterRepository
import com.aloysius.dicoding.data.remote.repository.user.UploadRepository
import com.aloysius.dicoding.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val registerRepository = RegisterRepository.getInstance(apiService)
        val loginRepository = LoginRepository.getInstance(apiService)
        return UserRepository.getInstance(userPreference, registerRepository, loginRepository)
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }

    fun provideStoryRepository(context: Context): StoryPagingRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        TokenManager.setToken(user.token)
        val apiService = ApiConfig.getApiService()
        val database = StoryDatabase.getDatabase(context)
        return StoryPagingRepository(database, apiService)
    }

    fun provideAddStoryRepository(context: Context): UploadRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        TokenManager.setToken(user.token)
        val apiService = ApiConfig.getApiService()
        return UploadRepository.getInstance(apiService)
    }

    fun provideMapsStoryRepository(context: Context): MapsRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getSession().first() }
        TokenManager.setToken(user.token)
        val apiService = ApiConfig.getApiService()
        return MapsRepository.getInstance(apiService)
    }

}


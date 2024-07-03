package com.aloysius.dicoding.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aloysius.dicoding.data.remote.repository.StoryPagingRepository
import com.aloysius.dicoding.data.remote.repository.UserRepository
import com.aloysius.dicoding.data.remote.repository.user.MapsRepository
import com.aloysius.dicoding.data.remote.repository.user.UploadRepository
import com.aloysius.dicoding.di.Injection
import com.aloysius.dicoding.view.login.LoginViewModel
import com.aloysius.dicoding.view.main.MainViewModel
import com.aloysius.dicoding.view.maps.MapsViewModel
import com.aloysius.dicoding.view.signup.RegisterViewModel
import com.aloysius.dicoding.view.upload.AddStoryViewModel
import kotlinx.coroutines.runBlocking

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val storyPagingRepository: StoryPagingRepository,
    private val uploadRepository: UploadRepository,
    private val mapsRepository: MapsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, storyPagingRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(uploadRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(mapsRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: runBlocking {
                    ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideStoryRepository(context),
                        Injection.provideAddStoryRepository(context),
                        Injection.provideMapsStoryRepository(context)
                    )
                }.also { INSTANCE = it }
            }
        }
    }
}

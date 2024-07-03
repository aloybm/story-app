package com.aloysius.dicoding.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aloysius.dicoding.data.pref.UserModel
import com.aloysius.dicoding.data.remote.repository.StoryPagingRepository
import com.aloysius.dicoding.data.remote.repository.UserRepository
import com.aloysius.dicoding.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    storyPagingRepository: StoryPagingRepository,
) : ViewModel() {

    val stories: LiveData<PagingData<ListStoryItem>> =
        storyPagingRepository.getStory().cachedIn(viewModelScope)

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}

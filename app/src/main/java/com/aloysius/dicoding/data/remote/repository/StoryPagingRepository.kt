@file:OptIn(ExperimentalPagingApi::class)

package com.aloysius.dicoding.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.aloysius.dicoding.data.database.StoryDatabase
import com.aloysius.dicoding.data.remote.repository.user.StoryRemoteMediator
import com.aloysius.dicoding.data.remote.response.ListStoryItem
import com.aloysius.dicoding.data.remote.retrofit.ApiService

class StoryPagingRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
               storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }
}
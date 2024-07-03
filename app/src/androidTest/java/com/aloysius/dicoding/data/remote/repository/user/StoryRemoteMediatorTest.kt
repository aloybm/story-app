package com.aloysius.dicoding.data.remote.repository.user

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aloysius.dicoding.data.database.StoryDatabase
import com.aloysius.dicoding.data.remote.response.ListStoryItem
import com.aloysius.dicoding.data.remote.response.StoryResponse
import com.aloysius.dicoding.data.remote.retrofit.ApiService
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(), StoryDatabase::class.java
    ).allowMainThreadQueries().build()


    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(), null, PagingConfig(10), 10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {

    override suspend fun getStories(page: Int, size: Int): Response<StoryResponse> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0 until size) {
            val story = ListStoryItem(
                id = "$i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "PhotoUrl $i",
                createdAt = "CreatedAt $i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(story)
        }
        val startIndex = (page - 1) * size
        val endIndex = (page - 1) * size + size
        val storyResponse = StoryResponse(
            listStory = if (endIndex <= items.size) items.subList(
                startIndex, endIndex
            ) else emptyList()
        )
        return Response.success(storyResponse)
    }

    override suspend fun register(name: String, email: String, password: String) =
        throw NotImplementedError()

    override suspend fun login(email: String, password: String) = throw NotImplementedError()
    override suspend fun getStoriesWithLocation(location: Int) = throw NotImplementedError()
    override suspend fun uploadImage(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?,
    ) = throw NotImplementedError()
}

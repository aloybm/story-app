package com.aloysius.dicoding.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aloysius.dicoding.data.ResultState
import com.aloysius.dicoding.data.remote.repository.user.MapsRepository
import com.aloysius.dicoding.data.remote.response.ListStoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: MapsRepository) : ViewModel() {

    private val _mapsData = MutableLiveData<ResultState<List<ListStoryItem>>>()
    val mapsData: LiveData<ResultState<List<ListStoryItem>>> = _mapsData

    fun getMaps() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _mapsData.postValue(ResultState.Loading)
                val result = repository.getMaps()
                _mapsData.postValue(result)
            } catch (e: Exception) {
                _mapsData.postValue(ResultState.Error(e.message!!))
            }
        }
    }
}
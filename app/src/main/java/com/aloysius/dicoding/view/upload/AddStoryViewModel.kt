package com.aloysius.dicoding.view.upload

import androidx.lifecycle.ViewModel
import com.aloysius.dicoding.data.remote.repository.user.UploadRepository
import java.io.File

class AddStoryViewModel(private val repository: UploadRepository) : ViewModel() {
    fun uploadImage(file: File, description: String, lat: Float?, lon: Float?) = repository.uploadImage(file, description, lat, lon)
}

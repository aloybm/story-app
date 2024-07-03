package com.aloysius.dicoding.data.remote.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
	@SerializedName("listStory")
	val listStory: List<ListStoryItem?>? = null,
)

@Entity(tableName = "story")
@Parcelize
data class ListStoryItem(
	@PrimaryKey val id: String,
	val name: String,
	val description: String,
	val photoUrl: String,
	val createdAt: String,
	val lat: Double,
	val lon: Double,
) : Parcelable
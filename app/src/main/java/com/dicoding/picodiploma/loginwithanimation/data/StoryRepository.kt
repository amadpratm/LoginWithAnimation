package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.StoryApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val storyApiService: StoryApiService
) {
    suspend fun getStories(): List<ListStoryItem> {
        val response = storyApiService.getStories()
        return if (response.error == false) {
            response.listStory?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Error fetching stories: ${response.message}")
        }
    }

    suspend fun uploadStory(
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) = storyApiService.uploadImage(multipartBody, description, lat, lon)

    companion object {
        fun getInstance(storyApiService: StoryApiService): StoryRepository {
            return StoryRepository(storyApiService)
        }
    }
}
package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.UploadResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UploadViewModel(
    private val storyRepository: StoryRepository, private val userRepository: UserRepository
) : ViewModel() {

    private val _uploadResponse = MutableLiveData<UploadResponse>()
    val uploadResponse: LiveData<UploadResponse> = _uploadResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUser(): LiveData<UserModel> = userRepository.getUser()

    fun uploadStory(
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = storyRepository.uploadStory(multipartBody, description, lat, lon)
                _uploadResponse.postValue(response)
                _isLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, UploadResponse::class.java)
                val errorMessage = errorBody.message

                _isLoading.postValue(false)
                _uploadResponse.postValue(errorBody)

                Log.d(TAG, "Upload File Error: $errorMessage")
            }
        }
    }



    companion object {
        private val TAG = ""
    }
}
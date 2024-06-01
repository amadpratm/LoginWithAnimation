package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository, private val storyRepository: StoryRepository) : ViewModel() {

    private val _userSession = MutableLiveData<UserModel>()
    private val _storyResponse = MutableLiveData<List<ListStoryItem>>()

    val storyResponse: LiveData<List<ListStoryItem>> get() = _storyResponse

    init {
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            repository.getSession().collect { user ->
                _userSession.value = user
            }
        }
    }

    fun getStories() {
        viewModelScope.launch {
            try {
                val listStory = storyRepository.getStories()
                _storyResponse.value = listStory
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}
package com.takehomeproject.presentation.repolist

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomeproject.data.model.UserResponse
import com.takehomeproject.data.repository.UserRepoRepository
import com.takehomeproject.domain.model.UserRepoUIState
import com.takehomeproject.util.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRepoListViewModel @Inject constructor(private val repository: UserRepoRepository) :
    ViewModel() {

    private val _userRepoUIState =
        MutableStateFlow<UserRepoUIState>(UserRepoUIState.Loading(View.GONE))
    val userRepoUIState: StateFlow<UserRepoUIState> = _userRepoUIState

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            repository.fetchUser(userId).collect { result ->
                when (result) {
                    is Results.Loading -> {
                        _userRepoUIState.value = UserRepoUIState.Loading(View.VISIBLE)
                    }

                    is Results.Success -> {
                        fetchUserRepoList(userId, result.data)
                    }

                    is Results.Error -> {
                        _userRepoUIState.value = UserRepoUIState.Error(result.exception)
                    }
                }
            }

        }
    }

    private suspend fun fetchUserRepoList(userId: String, userResponse: UserResponse) {
        repository.fetchUserRepoList(userId).collect { result ->
            when (result) {
                is Results.Loading -> {
                    //do nothing
                }

                is Results.Success -> {
                    _userRepoUIState.value = UserRepoUIState.Success(userResponse, result.data)
                }

                is Results.Error -> {
                    _userRepoUIState.value = UserRepoUIState.Error(result.exception)
                }
            }
        }
    }
}
package com.takehomeproject.domain.model

import com.takehomeproject.data.model.UserRepoResponse
import com.takehomeproject.data.model.UserResponse

sealed interface UserRepoUIState {
    data class Success(val userData: UserResponse, val repoList: List<UserRepoResponse>) :
        UserRepoUIState

    data class Error(val exception: Throwable) : UserRepoUIState
    data class Loading(val visibility: Int) : UserRepoUIState
}
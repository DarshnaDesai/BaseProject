package com.takehomeproject.data.repository

import androidx.annotation.WorkerThread
import com.takehomeproject.data.model.UserRepoResponse
import com.takehomeproject.data.model.UserResponse
import com.takehomeproject.data.network.Dispatcher
import com.takehomeproject.data.network.TakeHomeProjectAppDispatchers
import com.takehomeproject.data.network.UserRepoApiService
import com.takehomeproject.util.Results
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepoRepository @Inject constructor(
    private val apiService: UserRepoApiService,
    @Dispatcher(TakeHomeProjectAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    @WorkerThread
    suspend fun fetchUser(userId: String): Flow<Results<UserResponse>> =
        flow {
            emit(Results.Loading)
            try {
                val user = apiService.getUser(userId)
                emit(Results.Success(user))
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }.flowOn(ioDispatcher)

    @WorkerThread
    suspend fun fetchUserRepoList(userId: String): Flow<Results<List<UserRepoResponse>>> {
        return flow {
            emit(Results.Loading)
            try {
                val user = apiService.getUserRepoList(userId)
                emit(Results.Success(user))
            } catch (e: Exception) {
                emit(Results.Error(e))
            }
        }.flowOn(ioDispatcher)
    }
}

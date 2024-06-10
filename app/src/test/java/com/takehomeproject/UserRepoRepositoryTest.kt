package com.takehomeproject

import com.takehomeproject.data.model.UserRepoResponse
import com.takehomeproject.data.model.UserResponse
import com.takehomeproject.data.network.UserRepoApiService
import com.takehomeproject.data.repository.UserRepoRepository
import com.takehomeproject.util.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UserRepoRepositoryTest {

    @Mock
    private lateinit var apiService: UserRepoApiService

    private lateinit var repository: UserRepoRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = UserRepoRepository(apiService, testDispatcher)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `test successful user fetch`() = runTest {
        val userId = "user123"
        val userResponse = UserResponse("John Doe", "")

        doReturn(userResponse)
            .`when`(apiService)
            .getUser(userId)

        val result = repository.fetchUser(userId)
        advanceUntilIdle()

        // Collect all emitted results into a list
        val results = result.toList()

        assertEquals(Results.Loading, results[0])
        assertEquals(Results.Success(userResponse), results[1])
        assertEquals(2, results.size)
        verify(apiService).getUser(userId)
    }

    @Test
    fun `test successful user repo list fetch`() = runTest {
        val userId = "user123"
        val repoList = listOf<UserRepoResponse>()

        doReturn(repoList)
            .`when`(apiService)
            .getUserRepoList(userId)

        val result = repository.fetchUserRepoList(userId)
        advanceUntilIdle()

        // Collect all emitted results into a list
        val results = result.toList()

        assertEquals(Results.Loading, results[0])
        assertEquals(Results.Success(repoList), results[1])
        assertEquals(2, results.size)
        verify(apiService).getUserRepoList(userId)
    }

    @Test
    fun `test error handling - user fetch`() = runTest {
        val userId = "user123"
        val exception = RuntimeException("User not found")

        doThrow(exception).`when`(apiService)
            .getUser(userId)

        val result = repository.fetchUser(userId)
        advanceUntilIdle()

        // Collect all emitted results into a list
        val results = result.toList()

        assertEquals(Results.Loading, results[0])
        assertEquals(Results.Error(exception), results[1])
        assertEquals(2, results.size)
        verify(apiService).getUser(userId)
    }

    @Test
    fun `test error handling - user repo list fetch`() = runTest {
        val userId = "user123"
        val exception = RuntimeException("Failed to fetch repo list")

        doThrow(exception).`when`(apiService)
            .getUserRepoList(userId)

        val result = repository.fetchUserRepoList(userId)
        advanceUntilIdle()

        // Collect all emitted results into a list
        val results = result.toList()

        assertEquals(Results.Loading, results[0])
        assertEquals(Results.Error(exception), results[1])
        assertEquals(2, results.size)
        verify(apiService).getUserRepoList(userId)
    }

    @Test
    fun `test loading state - user fetch`() = runTest {
        val userId = "user123"
        val userResponse = UserResponse("John Doe", "")
        doReturn(userResponse)
            .`when`(apiService)
            .getUser(userId)

        val result = repository.fetchUser(userId)
        advanceUntilIdle()
        assertEquals(Results.Loading, result.first())

        verify(apiService).getUser(userId)
    }

    @Test
    fun `test loading state - user repo list fetch`() = runTest {
        val userId = "user123"
        val repoList = listOf<UserRepoRepository>()
        doReturn(repoList)
            .`when`(apiService)
            .getUserRepoList(userId)

        val result = repository.fetchUserRepoList(userId)
        advanceUntilIdle()
        assertEquals(Results.Loading, result.first())

        verify(apiService).getUserRepoList(userId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher to the original dispatcher
    }
}

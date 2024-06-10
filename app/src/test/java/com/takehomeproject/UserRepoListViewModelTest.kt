package com.takehomeproject

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.takehomeproject.data.model.UserRepoResponse
import com.takehomeproject.data.model.UserResponse
import com.takehomeproject.data.repository.UserRepoRepository
import com.takehomeproject.domain.model.UserRepoUIState
import com.takehomeproject.presentation.repolist.UserRepoListViewModel
import com.takehomeproject.util.Results
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UserRepoListViewModelTest {

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: UserRepoRepository

    private lateinit var viewModel: UserRepoListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = UserRepoListViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `fetchUser loading`() = runTest {
        // Given
        val userId = "123"
        val loadingResult = Results.Loading

        // Mock repository to emit loading result
        doReturn(flowOf(loadingResult))
            .`when`(repository)
            .fetchUser(userId)

        // When
        viewModel.fetchUser(userId)
        advanceUntilIdle()

        // Then
        assertEquals(UserRepoUIState.Loading(View.VISIBLE), viewModel.userRepoUIState.value) // Assert loading state
    }

    @Test
    fun `fetchUser success`() = runTest {
        // Given
        val userId = "123"
        val userResponse = UserResponse("John Doe", "df") // Mock user response
        val userRepoList = listOf<UserRepoResponse>() // Mock user response

        val successResult = Results.Success(userResponse)
        val successResult2 = Results.Success(userRepoList)

        // Mock repository to emit success result
        doReturn(flowOf(successResult))
            .`when`(repository)
            .fetchUser(userId)
        doReturn(flowOf(successResult2))
            .`when`(repository)
            .fetchUserRepoList(userId)

        // When
        viewModel.fetchUser(userId)
        advanceUntilIdle()

        // Then
        assertEquals(UserRepoUIState.Success(userResponse, userRepoList), viewModel.userRepoUIState.value) // Assert user data
    }


    @Test
    fun `fetchUser error`() = runTest {
        // Given
        val userId = "123"
        val errorMessage = "Error fetching user"
        val errorResult = Results.Error(Throwable(errorMessage))

        // Mock repository to emit error result
        doReturn(flowOf(errorResult))
            .`when`(repository)
            .fetchUser(userId)

        // When
        viewModel.fetchUser(userId)
        advanceUntilIdle()

        // Then
        assertEquals(UserRepoUIState.Error(errorResult.exception), viewModel.userRepoUIState.value) // Assert error message
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher to the original dispatcher
    }
}
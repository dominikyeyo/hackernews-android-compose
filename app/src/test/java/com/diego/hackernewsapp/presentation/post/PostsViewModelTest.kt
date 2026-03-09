package com.diego.hackernewsapp.presentation.post

import com.diego.hackernewsapp.domain.model.Post
import com.diego.hackernewsapp.domain.repository.PostRepository
import com.diego.hackernewsapp.domain.usecase.DeletePostUseCase
import com.diego.hackernewsapp.domain.usecase.GetPostsUseCase
import com.diego.hackernewsapp.domain.usecase.RefreshPostsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostsViewModelTest {

    @get:Rule
    val mainDispatcherRule = ResetMainDispatcherRule()

    @Test
    fun `when getPostsUseCase emits posts then uiState updates posts`() = runTest {
        val expectedPosts = listOf(
            Post(
                id = "1",
                title = "Post 1",
                author = "Diego",
                createdAt = "2026-03-09T10:00:00Z",
                url = "https://example.com/1",
                isDeleted = false
            )
        )

        val repository = FakePostRepository(
            postsFlow = flowOf(expectedPosts)
        )

        val viewModel = buildViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.posts.size)
        assertEquals("Post 1", state.posts.first().title)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `when refreshPosts succeeds then loading becomes false`() = runTest {
        val repository = FakePostRepository(
            postsFlow = flowOf(emptyList()),
            refreshShouldFail = false
        )

        val viewModel = buildViewModel(repository)

        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.error)
        assertTrue(repository.refreshCalled)
    }

    @Test
    fun `when refreshPosts fails then uiState contains error`() = runTest {
        val repository = FakePostRepository(
            postsFlow = flowOf(emptyList()),
            refreshShouldFail = true
        )

        val viewModel = buildViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Refresh failed", state.error)
    }

    @Test
    fun `when deletePost is called then repository receives correct id`() = runTest {
        val repository = FakePostRepository(
            postsFlow = flowOf(emptyList())
        )

        val viewModel = buildViewModel(repository)

        viewModel.deletePost("post-123")
        advanceUntilIdle()

        assertEquals("post-123", repository.deletedPostId)
    }

    @Test
    fun `when posts flow emits later then uiState updates with latest posts`() = runTest {
        val postsFlow = MutableSharedFlow<List<Post>>(replay = 1)
        val repository = FakePostRepository(postsFlow = postsFlow)

        val viewModel = buildViewModel(repository)

        val firstPosts = listOf(
            Post(
                id = "1",
                title = "First",
                author = "Author 1",
                createdAt = "2026-03-09T11:00:00Z",
                url = "https://example.com/first",
                isDeleted = false
            )
        )

        val secondPosts = listOf(
            Post(
                id = "2",
                title = "Second",
                author = "Author 2",
                createdAt = "2026-03-09T12:00:00Z",
                url = "https://example.com/second",
                isDeleted = false
            )
        )

        postsFlow.emit(firstPosts)
        postsFlow.emit(secondPosts)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.posts.size)
        assertEquals("Second", state.posts.first().title)
    }

    private fun buildViewModel(
        repository: FakePostRepository
    ): PostsViewModel {
        return PostsViewModel(
            getPostsUseCase = GetPostsUseCase(repository),
            refreshPostsUseCase = RefreshPostsUseCase(repository),
            deletePostUseCase = DeletePostUseCase(repository)
        )
    }

    private class FakePostRepository(
        private val postsFlow: Flow<List<Post>>,
        private val refreshShouldFail: Boolean = false
    ) : PostRepository {

        var refreshCalled: Boolean = false
        var deletedPostId: String? = null

        override fun getPosts(): Flow<List<Post>> = postsFlow

        override suspend fun refreshPosts() {
            refreshCalled = true
            if (refreshShouldFail) {
                throw IllegalStateException("Refresh failed")
            }
        }

        override suspend fun deletePost(postId: String) {
            deletedPostId = postId
        }
    }
}
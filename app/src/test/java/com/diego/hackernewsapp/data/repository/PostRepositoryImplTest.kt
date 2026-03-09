package com.diego.hackernewsapp.data.repository

import com.diego.hackernewsapp.data.local.dao.PostDao
import com.diego.hackernewsapp.data.local.entity.PostEntity
import com.diego.hackernewsapp.data.remote.api.HackerNewsApi
import com.diego.hackernewsapp.data.remote.dto.PostDto
import com.diego.hackernewsapp.data.remote.dto.PostResponseDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostRepositoryImplTest {

    @Test
    fun `getPosts maps local entities to domain models`() = runTest {
        val localPosts = listOf(
            PostEntity(
                id = "1",
                title = "Local title",
                author = "Diego",
                createdAt = "2026-03-09T10:00:00Z",
                url = "https://example.com/1",
                isDeleted = false
            )
        )

        val dao = FakePostDao(initialPosts = localPosts)
        val api = FakeHackerNewsApi()
        val repository = PostRepositoryImpl(api, dao)

        val result = repository.getPosts().first()

        assertEquals(1, result.size)
        assertEquals("1", result.first().id)
        assertEquals("Local title", result.first().title)
        assertEquals("Diego", result.first().author)
        assertEquals("https://example.com/1", result.first().url)
        assertEquals(false, result.first().isDeleted)
    }

    @Test
    fun `refreshPosts inserts remote posts into dao`() = runTest {
        val dao = FakePostDao()
        val api = FakeHackerNewsApi(
            response = PostResponseDto(
                hits = listOf(
                    PostDto(
                        objectId = "1",
                        storyTitle = "Remote title",
                        title = null,
                        author = "Author",
                        createdAt = "2026-03-09T11:00:00Z",
                        storyUrl = "https://example.com/remote",
                        url = null
                    )
                )
            )
        )

        val repository = PostRepositoryImpl(api, dao)

        repository.refreshPosts()

        assertEquals(1, dao.insertedPosts.size)
        assertEquals("1", dao.insertedPosts.first().id)
        assertEquals("Remote title", dao.insertedPosts.first().title)
    }

    @Test
    fun `refreshPosts filters deleted post ids`() = runTest {
        val dao = FakePostDao(
            deletedIds = listOf("1")
        )

        val api = FakeHackerNewsApi(
            response = PostResponseDto(
                hits = listOf(
                    PostDto(
                        objectId = "1",
                        storyTitle = "Deleted remote post",
                        title = null,
                        author = "Author 1",
                        createdAt = "2026-03-09T11:00:00Z",
                        storyUrl = "https://example.com/deleted",
                        url = null
                    ),
                    PostDto(
                        objectId = "2",
                        storyTitle = "Valid remote post",
                        title = null,
                        author = "Author 2",
                        createdAt = "2026-03-09T12:00:00Z",
                        storyUrl = "https://example.com/valid",
                        url = null
                    )
                )
            )
        )

        val repository = PostRepositoryImpl(api, dao)

        repository.refreshPosts()

        assertEquals(1, dao.insertedPosts.size)
        assertEquals("2", dao.insertedPosts.first().id)
        assertEquals("Valid remote post", dao.insertedPosts.first().title)
    }

    @Test
    fun `deletePost marks post as deleted in dao`() = runTest {
        val dao = FakePostDao()
        val api = FakeHackerNewsApi()
        val repository = PostRepositoryImpl(api, dao)

        repository.deletePost("post-123")

        assertEquals("post-123", dao.markedDeletedPostId)
    }

    private class FakePostDao(
        initialPosts: List<PostEntity> = emptyList(),
        private val deletedIds: List<String> = emptyList()
    ) : PostDao {

        private var posts: List<PostEntity> = initialPosts

        val insertedPosts = mutableListOf<PostEntity>()
        var markedDeletedPostId: String? = null

        override fun observePosts(): Flow<List<PostEntity>> = flowOf(posts)

        override suspend fun insertPosts(posts: List<PostEntity>) {
            insertedPosts.clear()
            insertedPosts.addAll(posts)
            this.posts = posts
        }

        override suspend fun markAsDeleted(postId: String) {
            markedDeletedPostId = postId
        }

        override suspend fun getDeletedPostIds(): List<String> = deletedIds
    }

    private class FakeHackerNewsApi(
        private val response: PostResponseDto = PostResponseDto(hits = emptyList())
    ) : HackerNewsApi {

        override suspend fun getMobilePosts(query: String): PostResponseDto {
            return response
        }
    }
}
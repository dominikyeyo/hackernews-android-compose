package com.diego.hackernewsapp.data.repository

import com.diego.hackernewsapp.data.local.dao.PostDao
import com.diego.hackernewsapp.data.mapper.toDomain
import com.diego.hackernewsapp.data.mapper.toEntity
import com.diego.hackernewsapp.data.remote.api.HackerNewsApi
import com.diego.hackernewsapp.domain.model.Post
import com.diego.hackernewsapp.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: HackerNewsApi,
    private val dao: PostDao
) : PostRepository {

    override fun getPosts(): Flow<List<Post>> {
        return dao.observePosts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshPosts() {

        val deletedPostIds = dao.getDeletedPostIds()

        val remotePosts = api.getMobilePosts()
            .hits.orEmpty()
            .mapNotNull { it.toEntity() }
            .filterNot { it.id in deletedPostIds }

        dao.insertPosts(remotePosts)
    }

    override suspend fun deletePost(postId: String) {
        dao.markAsDeleted(postId)
    }
}
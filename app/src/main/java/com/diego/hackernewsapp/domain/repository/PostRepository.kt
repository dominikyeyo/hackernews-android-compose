package com.diego.hackernewsapp.domain.repository

import com.diego.hackernewsapp.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    suspend fun refreshPosts()
    suspend fun deletePost(postId: String)
}
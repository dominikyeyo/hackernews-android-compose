package com.diego.hackernewsapp.domain.usecase

import com.diego.hackernewsapp.domain.model.Post
import com.diego.hackernewsapp.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    operator fun invoke(): Flow<List<Post>> {
        return repository.getPosts()
    }
}
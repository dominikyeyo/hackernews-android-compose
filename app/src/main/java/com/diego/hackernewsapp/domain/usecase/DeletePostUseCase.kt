package com.diego.hackernewsapp.domain.usecase

import com.diego.hackernewsapp.domain.repository.PostRepository
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: String) {
        repository.deletePost(postId)
    }
}
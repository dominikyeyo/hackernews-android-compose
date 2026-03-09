package com.diego.hackernewsapp.domain.usecase

import com.diego.hackernewsapp.domain.repository.PostRepository
import javax.inject.Inject

class RefreshPostsUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke() {
        repository.refreshPosts()
    }
}
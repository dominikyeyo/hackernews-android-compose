package com.diego.hackernewsapp.presentation.post

import com.diego.hackernewsapp.domain.model.Post

data class PostsUiState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String? = null
)
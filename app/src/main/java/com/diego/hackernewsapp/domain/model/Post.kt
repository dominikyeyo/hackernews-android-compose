package com.diego.hackernewsapp.domain.model

data class Post(
    val id: String,
    val title: String,
    val author: String,
    val createdAt: String,
    val url: String,
    val isDeleted: Boolean = false
)
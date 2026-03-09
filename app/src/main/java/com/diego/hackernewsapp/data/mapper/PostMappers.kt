package com.diego.hackernewsapp.data.mapper

import com.diego.hackernewsapp.data.local.entity.PostEntity
import com.diego.hackernewsapp.data.remote.dto.PostDto
import com.diego.hackernewsapp.domain.model.Post

fun PostDto.toEntity(): PostEntity? {
    val resolvedId = objectId ?: return null
    val resolvedTitle = storyTitle ?: title ?: return null
    val resolvedUrl = storyUrl ?: url ?: return null

    return PostEntity(
        id = resolvedId,
        title = resolvedTitle,
        author = author.orEmpty(),
        createdAt = createdAt.orEmpty(),
        url = resolvedUrl,
        isDeleted = false
    )
}

fun PostEntity.toDomain(): Post {
    return Post(
        id = id,
        title = title,
        author = author,
        createdAt = createdAt,
        url = url,
        isDeleted = isDeleted
    )
}
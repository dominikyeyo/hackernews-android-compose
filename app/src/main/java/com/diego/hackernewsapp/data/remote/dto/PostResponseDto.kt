package com.diego.hackernewsapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostResponseDto(
    @Json(name = "hits")
    val hits: List<PostDto>?
)
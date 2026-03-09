package com.diego.hackernewsapp.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostDto(
    @Json(name = "objectID")
    val objectId: String?,

    @Json(name = "story_title")
    val storyTitle: String?,

    @Json(name = "title")
    val title: String?,

    @Json(name = "story_url")
    val storyUrl: String?,

    @Json(name = "url")
    val url: String?,

    @Json(name = "author")
    val author: String?,

    @Json(name = "created_at")
    val createdAt: String?
)
package com.diego.hackernewsapp.data.remote.api

import com.diego.hackernewsapp.data.remote.dto.PostsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HackerNewsApi {

    @GET("api/v1/search_by_date")
    suspend fun getMobilePosts(
        @Query("query") query: String = "mobile"
    ): PostsResponseDto
}
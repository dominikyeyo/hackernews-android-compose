package com.diego.hackernewsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val createdAt: String,
    val url: String,
    val isDeleted: Boolean
)
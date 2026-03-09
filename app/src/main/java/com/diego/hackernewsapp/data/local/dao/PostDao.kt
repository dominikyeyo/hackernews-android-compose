package com.diego.hackernewsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.diego.hackernewsapp.data.local.entity.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM posts WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun observePosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("UPDATE posts SET isDeleted = 1 WHERE id = :postId")
    suspend fun markAsDeleted(postId: String)

    @Query("SELECT id FROM posts WHERE isDeleted = 1")
    suspend fun getDeletedPostIds(): List<String>
}
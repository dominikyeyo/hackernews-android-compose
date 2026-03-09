package com.diego.hackernewsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.diego.hackernewsapp.data.local.dao.PostDao
import com.diego.hackernewsapp.data.local.entity.PostEntity

@Database(
    entities = [PostEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
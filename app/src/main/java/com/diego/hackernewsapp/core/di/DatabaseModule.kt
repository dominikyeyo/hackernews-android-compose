package com.diego.hackernewsapp.core.di

import android.content.Context
import androidx.room.Room
import com.diego.hackernewsapp.core.database.AppDatabase
import com.diego.hackernewsapp.data.local.dao.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "hacker_news_db"
        ).build()
    }

    @Provides
    fun providePostDao(
        database: AppDatabase
    ): PostDao {
        return database.postDao()
    }
}
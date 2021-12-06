package com.example.appcar.di

import android.content.Context
import androidx.room.Room
import com.example.appcar.data.db.*
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app.db"
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideRoadDao(database: AppDatabase): RoadDao {
        return database.roadDao()
    }

    @Provides
    fun provideCarType(database: AppDatabase): CarTypeDao {
        return database.carTypeDao()
    }

    @Provides
    fun provideNoticeBoard(database: AppDatabase): NoticeBoardDao {
        return database.noticeBoardDao()
    }

    @Provides
    fun provideNoticeTypeDao(database: AppDatabase): NoticeTypeDao {
        return database.noticeTypeDao()
    }

}
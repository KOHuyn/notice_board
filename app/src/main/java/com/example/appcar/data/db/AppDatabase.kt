package com.example.appcar.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appcar.data.model.*

@Database(
    entities = [Road::class, CarType::class, RelationRoadCar::class, NoticeBoard::class, NoticeType::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roadDao(): RoadDao
    abstract fun carTypeDao(): CarTypeDao
    abstract fun noticeTypeDao(): NoticeTypeDao
    abstract fun noticeBoardDao(): NoticeBoardDao
}
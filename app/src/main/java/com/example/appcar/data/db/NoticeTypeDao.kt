package com.example.appcar.data.db

import androidx.room.*
import com.example.appcar.data.model.CarType
import com.example.appcar.data.model.NoticeType
import com.example.appcar.data.model.RelationRoadCar
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeTypeDao {
    @Query("SELECT * FROM notice_type")
    fun getAll(): Flow<List<NoticeType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(noticeTypeList: List<NoticeType>)

    @Query("SELECT EXISTS (SELECT * FROM notice_type LIMIT 1)")
    suspend fun isNotEmpty(): Boolean
}
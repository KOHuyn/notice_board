package com.example.appcar.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appcar.data.model.NoticeBoard
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeBoardDao {
    @Query("SELECT * FROM noticeboard  WHERE type_notice IN (:type)")
    fun getNoticeBoardDao(type: Int): Flow<List<NoticeBoard>>

    @Query("SELECT * FROM noticeboard  WHERE type_notice IN (:type) AND name LIKE '%' ||:search||'%'")
    fun searchNotice(type: Int, search: String): Flow<List<NoticeBoard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listNotice: List<NoticeBoard>)

    @Query("SELECT EXISTS (SELECT * FROM noticeboard LIMIT 1)")
    suspend fun isNotEmpty(): Boolean

    @Query("DELETE FROM NoticeBoard")
    suspend fun deleteAll()

}
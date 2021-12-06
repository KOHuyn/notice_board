package com.example.appcar.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appcar.data.model.Road
import kotlinx.coroutines.flow.Flow

@Dao
interface RoadDao {
    @Query("SELECT * FROM road")
    fun getAll(): Flow<List<Road>>

    @Query("SELECT * FROM road WHERE is_favourite = 1")
    fun getAllRoadFav(): Flow<List<Road>>

    @Query("SELECT * FROM road WHERE type IN (:type)")
    fun getRoadByType(type: List<Int>): Flow<List<Road>>

    @Query("SELECT * FROM road WHERE name LIKE '%' ||:search|| '%' OR description LIKE '%' ||:search|| '%'")
    fun searchRoad(search: String): Flow<List<Road>>

    @Query("SELECT * FROM road WHERE type in (:type) AND (name LIKE '%' ||:search|| '%' OR description LIKE '%' ||:search|| '%')")
    fun searchRoadByType(type: List<Int>, search: String): Flow<List<Road>>

    @Query("SELECT id FROM road WHERE is_favourite = 1")
    suspend fun getAllRoadFavIds(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(roadList: List<Road>)

    @Delete
    suspend fun delete(road: Road)

    @Update
    suspend fun updateRoad(road: Road)

    @Query("SELECT EXISTS( SELECT * from road LIMIT 1)")
    suspend fun isNotEmpty(): Boolean

}
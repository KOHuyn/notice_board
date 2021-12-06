package com.example.appcar.data.db

import androidx.room.*
import com.example.appcar.data.model.CarType
import kotlinx.coroutines.flow.Flow

@Dao
interface CarTypeDao {
    @Query("SELECT * FROM cartype")
    fun getAll(): Flow<List<CarType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(carList: List<CarType>)

    @Query("SELECT EXISTS (SELECT * FROM cartype LIMIT 1)")
    suspend fun isNotEmpty(): Boolean

    @Delete
    suspend fun delete(carType: CarType)
}
package com.example.appcar.data.repository

import com.example.appcar.data.anotation.FirebaseRoadDb
import com.example.appcar.data.db.AppDatabase
import com.example.appcar.data.db.RoadDao
import com.example.appcar.data.firebase.DbConstants
import com.example.appcar.data.model.Road
import com.example.appcar.util.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by KO Huyn on 29/11/2021.
 */
class RoadRepository @Inject constructor(
    @FirebaseRoadDb val roadDb: DatabaseReference,
    val roadDao: RoadDao
) {
    init {
        Timber.i("[initialize] RoadRepository")
    }

    fun updateRoadFromApi(isRefresh: Boolean): Flow<BaseResponse<List<Road>>> {
        Timber.d("updateRoadFromApi")
        return flow<BaseResponse<List<Road>>> {
            emit(if (isRefresh) StatusRefresh() else StatusLoading())
            try {
                val arr: List<Road> =
                    roadDb.get().await().getValue<List<Road?>>()?.filterNotNull() ?: listOf()
                if (arr.isNotEmpty()) {
                    val isFavOldIds = roadDao.getAllRoadFavIds()
                    arr.map { it.isFavorite = isFavOldIds.contains(it.id) }
                    emit(BaseResponse.Success(arr))
                    roadDao.insertAll(arr)
                } else {
                    emit(StatusEmpty())
                }
            } catch (e: Exception) {
                emit(StatusError(e))
            }
        }
    }

    fun getAllRoadFavorite(): Flow<List<Road>> = roadDao.getAllRoadFav()

    fun getAllRoadDb() = roadDao.getAll()

    suspend fun isDbEmpty() = roadDao.isNotEmpty().not()

    suspend fun updateRoad(road: Road) = roadDao.updateRoad(road)

    fun searchRoad(type: List<Int>, search: String?): Flow<List<Road>> {
        return if (search == null) {
            roadDao.getRoadByType(type)
        } else {
            if (type.isEmpty()) {
                roadDao.searchRoad(search)
            } else {
                roadDao.searchRoadByType(type, search)
            }
        }
    }
}
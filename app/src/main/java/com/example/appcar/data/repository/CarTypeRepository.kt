package com.example.appcar.data.repository

import com.example.appcar.data.anotation.FirebaseCarTypeDb
import com.example.appcar.data.db.CarTypeDao
import com.example.appcar.data.model.CarType
import com.example.appcar.util.BaseResponse
import com.example.appcar.util.StatusEmpty
import com.example.appcar.util.StatusError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Created by KO Huyn on 03/12/2021.
 */
class CarTypeRepository @Inject constructor(
    @FirebaseCarTypeDb private val carTypeDb: DatabaseReference,
    private val carTypeDao: CarTypeDao
) {
    fun getAllFromFirebase() = flow<BaseResponse<List<CarType>>> {
        try {
            val arrCarType =
                carTypeDb.get().await().getValue<List<CarType?>>()?.filterNotNull() ?: listOf()
            if (arrCarType.isEmpty()) {
                emit(StatusEmpty())
            } else {
                carTypeDao.insertAll(arrCarType)
                emit(BaseResponse.Success(arrCarType))
            }
        } catch (e: Exception) {
            emit(StatusError(e))
        }
    }

    fun getAllCarType() = carTypeDao.getAll()

    suspend fun isDbEmpty() = carTypeDao.isNotEmpty().not()
}
package com.example.appcar.data.repository

import com.example.appcar.data.anotation.FirebaseNoticeTypeDb
import com.example.appcar.data.db.NoticeTypeDao
import com.example.appcar.data.model.NoticeType
import com.example.appcar.util.BaseResponse
import com.example.appcar.util.StatusEmpty
import com.example.appcar.util.StatusError
import com.example.appcar.util.StatusLoading
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by KO Huyn on 05/12/2021.
 */
class NoticeTypeRepository @Inject constructor(
    @FirebaseNoticeTypeDb private val noticeTypeDb: DatabaseReference,
    private val noticeTypeDao: NoticeTypeDao
) {
    fun getAllFromFirebase() = flow<BaseResponse<List<NoticeType>>> {
        emit(StatusLoading())
        try {
            val arrNoticeType =
                noticeTypeDb.get().await().getValue<List<NoticeType?>>()?.filterNotNull()
                    ?: listOf()
            if (arrNoticeType.isEmpty()) {
                emit(StatusEmpty())
            } else {
                noticeTypeDao.insertAll(arrNoticeType)
                emit(BaseResponse.Success(arrNoticeType))
            }
        } catch (e: Exception) {
            emit(StatusError(e))
        }
    }

    fun getAllNoticeType() = noticeTypeDao.getAll()

    suspend fun isDbEmpty() = noticeTypeDao.isNotEmpty().not()
}